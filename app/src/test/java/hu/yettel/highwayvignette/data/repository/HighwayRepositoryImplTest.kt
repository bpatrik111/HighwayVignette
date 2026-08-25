package hu.yettel.highwayvignette.data.repository

import hu.yettel.highwayvignette.data.remote.HighwayApi
import hu.yettel.highwayvignette.data.remote.dto.CountyDto
import hu.yettel.highwayvignette.data.remote.dto.HighwayInfoPayloadDto
import hu.yettel.highwayvignette.data.remote.dto.HighwayInfoResponseDto
import hu.yettel.highwayvignette.data.remote.dto.HighwayOrderResponseDto
import hu.yettel.highwayvignette.data.remote.dto.HighwayVignetteDto
import hu.yettel.highwayvignette.data.remote.dto.LocalizedNameDto
import hu.yettel.highwayvignette.data.remote.dto.VehicleCategoryDto
import hu.yettel.highwayvignette.data.remote.dto.VehicleResponseDto
import hu.yettel.highwayvignette.domain.model.OrderLineItem
import hu.yettel.highwayvignette.domain.model.OrderResult
import io.mockk.coEvery
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HighwayRepositoryImplTest {

    private lateinit var api: HighwayApi
    private lateinit var repository: HighwayRepositoryImpl

    // Mirrors the shape of the shared mock server response (see api/index.php):
    // a mix of national duration codes (DAY/WEEK/MONTH/YEAR) and one entry
    // whose vignetteType lists all 19 county ids.
    private val sampleInfoResponse = HighwayInfoResponseDto(
        requestId = 123L,
        statusCode = "OK",
        payload = HighwayInfoPayloadDto(
            highwayVignettes = listOf(
                HighwayVignetteDto(vignetteType = listOf("DAY"), vehicleCategory = "CAR", cost = 5150.0, trxFee = 200.0, sum = 5350.0),
                HighwayVignetteDto(vignetteType = listOf("WEEK"), vehicleCategory = "CAR", cost = 6400.0, trxFee = 200.0, sum = 6600.0),
                HighwayVignetteDto(vignetteType = listOf("MONTH"), vehicleCategory = "CAR", cost = 10360.0, trxFee = 200.0, sum = 10560.0),
                HighwayVignetteDto(vignetteType = listOf("YEAR"), vehicleCategory = "CAR", cost = 6660.0, trxFee = 200.0, sum = 6860.0),
                HighwayVignetteDto(
                    vignetteType = listOf("YEAR_11", "YEAR_12", "YEAR_13"),
                    vehicleCategory = "CAR",
                    cost = 6660.0,
                    trxFee = 200.0,
                    sum = 6860.0
                )
            ),
            vehicleCategories = listOf(
                VehicleCategoryDto(category = "CAR", vignetteCategory = "D1", name = LocalizedNameDto(hu = "Autó", en = "Car"))
            ),
            counties = listOf(
                CountyDto(id = "YEAR_11", name = "Bács-Kiskun"),
                CountyDto(id = "YEAR_12", name = "Baranya"),
                CountyDto(id = "YEAR_13", name = "Békés")
            )
        )
    )

    @Before
    fun setUp() {
        api = mockk()
        repository = HighwayRepositoryImpl(api)
    }

    @Test
    fun `getVehicle maps DTO fields to the domain model correctly`() = runTest {
        coEvery { api.getVehicleInfo() } returns VehicleResponseDto(
            requestId = 1L,
            statusCode = "OK",
            internationalRegistrationCode = "H",
            type = "CAR",
            name = "Michael Scott",
            plate = "abc-123",
            country = LocalizedNameDto(hu = "Magyarország", en = "Hungary"),
            vignetteType = "D1"
        )

        val vehicle = repository.getVehicle()

        assertEquals("abc-123", vehicle.plate)
        assertEquals("Michael Scott", vehicle.ownerName)
        assertEquals("CAR", vehicle.vehicleType)
    }

    @Test
    fun `getNationalVignetteOptions returns only the four national duration codes`() = runTest {
        coEvery { api.getHighwayInfo() } returns sampleInfoResponse

        val options = repository.getNationalVignetteOptions()

        val ids = options.map { it.id }.toSet()
        assertEquals(setOf("DAY", "WEEK", "MONTH", "YEAR"), ids)
        // The county-only entry (vignetteType listing YEAR_11/12/13) must not
        // leak into the national options list.
        assertTrue(ids.none { it.startsWith("YEAR_") })
    }

    @Test
    fun `getNationalVignetteOptions maps price fields correctly for a known entry`() = runTest {
        coEvery { api.getHighwayInfo() } returns sampleInfoResponse

        val dayOption = repository.getNationalVignetteOptions().first { it.id == "DAY" }

        assertEquals(5150.0, dayOption.cost, 0.0)
        assertEquals(200.0, dayOption.trxFee, 0.0)
        assertEquals(5350.0, dayOption.sum, 0.0)
    }

    @Test
    fun `getCounties returns all counties from the payload`() = runTest {
        coEvery { api.getHighwayInfo() } returns sampleInfoResponse

        val counties = repository.getCounties()

        assertEquals(3, counties.size)
        assertEquals("Bács-Kiskun", counties.first { it.id == "YEAR_11" }.name)
    }

    @Test
    fun `getCountyVignettePrice finds the entry whose vignetteType lists county ids`() = runTest {
        coEvery { api.getHighwayInfo() } returns sampleInfoResponse

        val price = repository.getCountyVignettePrice()

        assertEquals(6660.0, price.cost, 0.0)
        assertEquals(200.0, price.trxFee, 0.0)
    }

    @Test
    fun `placeOrder returns Success when the server responds with OK`() = runTest {
        coEvery { api.postOrder(any()) } returns HighwayOrderResponseDto(
            requestId = 42L,
            statusCode = "OK",
            receivedOrders = emptyList(),
            message = null
        )

        val result = repository.placeOrder(listOf(OrderLineItem(type = "DAY", category = "CAR", cost = 5150.0)))

        assertTrue(result is OrderResult.Success)
        assertEquals(42L, (result as OrderResult.Success).requestId)
    }

    @Test
    fun `placeOrder returns Failure when the server responds with ERROR`() = runTest {
        coEvery { api.postOrder(any()) } returns HighwayOrderResponseDto(
            requestId = 42L,
            statusCode = "ERROR",
            receivedOrders = null,
            message = "Invalid or missing highwayOrders parameter"
        )

        val result = repository.placeOrder(listOf(OrderLineItem(type = "DAY", category = "CAR", cost = 5150.0)))

        assertTrue(result is OrderResult.Failure)
        assertEquals("Invalid or missing highwayOrders parameter", (result as OrderResult.Failure).message)
    }

    @Test
    fun `placeOrder returns Failure when the network call throws`() = runTest {
        coEvery { api.postOrder(any()) } throws IOException("no connection")

        val result = repository.placeOrder(listOf(OrderLineItem(type = "DAY", category = "CAR", cost = 5150.0)))

        assertTrue(result is OrderResult.Failure)
    }
}