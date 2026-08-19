package hu.yettel.highwayvignette.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class HighwayOrderResponseDto(
    val requestId: Long,
    val statusCode: String,
    val receivedOrders: List<HighwayOrderItemDto>? = null,
    val message: String? = null
)