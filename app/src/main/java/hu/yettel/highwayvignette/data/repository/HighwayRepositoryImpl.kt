package hu.yettel.highwayvignette.data.repository

import hu.yettel.highwayvignette.data.remote.HighwayApi
import hu.yettel.highwayvignette.data.remote.dto.HighwayOrderRequestDto
import hu.yettel.highwayvignette.data.remote.mapper.toDomain
import hu.yettel.highwayvignette.data.remote.mapper.toDomainOptions
import hu.yettel.highwayvignette.data.remote.mapper.toDto
import hu.yettel.highwayvignette.domain.model.OrderLineItem
import hu.yettel.highwayvignette.domain.model.OrderResult
import hu.yettel.highwayvignette.domain.model.Vehicle
import hu.yettel.highwayvignette.domain.model.VignetteOption
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HighwayRepositoryImpl @Inject constructor(
    private val api: HighwayApi
) : HighwayRepository {

    override suspend fun getVehicle(): Vehicle = api.getVehicleInfo().toDomain()

    override suspend fun getNationalVignetteOptions(): List<VignetteOption> {
        val vignettes = api.getHighwayInfo().payload.highwayVignettes
        return vignettes
            .filter { entry -> entry.vignetteType.any { it in NATIONAL_CODES } }
            .flatMap { it.toDomainOptions() }
    }

    override suspend fun placeOrder(items: List<OrderLineItem>): OrderResult {
        val request = HighwayOrderRequestDto(highwayOrders = items.map { it.toDto() })
        return try {
            val response = api.postOrder(request)
            if (response.statusCode == "OK") {
                OrderResult.Success(
                    requestId = response.requestId,
                    receivedOrders = response.receivedOrders.orEmpty().map { it.toDomain() }
                )
            } else {
                OrderResult.Failure(response.message ?: "Unknown error.")
            }
        } catch (e: Exception) {
            OrderResult.Failure("Could not reach the server.")
        }
    }

    companion object {
        private val NATIONAL_CODES = setOf("DAY", "WEEK", "MONTH", "YEAR")
    }
}