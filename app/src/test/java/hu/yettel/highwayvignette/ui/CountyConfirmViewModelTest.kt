package hu.yettel.highwayvignette.ui

import androidx.lifecycle.SavedStateHandle
import hu.yettel.highwayvignette.data.repository.HighwayRepository
import hu.yettel.highwayvignette.domain.model.County
import hu.yettel.highwayvignette.domain.model.OrderResult
import hu.yettel.highwayvignette.domain.model.Vehicle
import hu.yettel.highwayvignette.domain.model.LocalizedText
import hu.yettel.highwayvignette.domain.model.VignetteOption
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CountyConfirmViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: HighwayRepository
    private lateinit var viewModel: CountyConfirmViewModel

    private val sampleVehicle = Vehicle(
        plate = "abc-123",
        ownerName = "Michael Scott",
        vehicleType = "CAR",
        country = LocalizedText(hu = "Magyarország", en = "Hungary"),
        currentVignetteType = "D1"
    )
    private val sampleCounties = listOf(
        County(id = "YEAR_11", name = "Bács-Kiskun"),
        County(id = "YEAR_12", name = "Baranya")
    )
    private val samplePrice = VignetteOption(id = "YEAR_11", vehicleCategory = "CAR", cost = 6660.0, trxFee = 200.0, sum = 6860.0)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        coEvery { repository.getVehicle() } returns sampleVehicle
        coEvery { repository.getCounties() } returns sampleCounties
        coEvery { repository.getCountyVignettePrice() } returns samplePrice

        val savedStateHandle = SavedStateHandle(mapOf("countyIds" to "YEAR_11,YEAR_12"))
        viewModel = CountyConfirmViewModel(repository, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle() // let init{} finish loading
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `calling confirmOrder again while submitting does not trigger a second network call`() = runTest {
        coEvery { repository.placeOrder(any()) } returns OrderResult.Success(requestId = 1L, receivedOrders = emptyList())

        viewModel.confirmOrder()
        viewModel.confirmOrder() // rapid double-tap simulation

        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { repository.placeOrder(any()) }
    }

    @Test
    fun `init loads vehicle plate and filters selected counties from all counties`() = runTest {
        assertTrue(viewModel.state.value.plate == "abc-123")
        assertTrue(viewModel.state.value.selectedCounties.map { it.id } == listOf("YEAR_11", "YEAR_12"))
    }
}