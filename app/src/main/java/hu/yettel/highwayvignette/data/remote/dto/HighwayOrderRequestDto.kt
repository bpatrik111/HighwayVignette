package hu.yettel.highwayvignette.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class HighwayOrderRequestDto(
    val highwayOrders: List<HighwayOrderItemDto>
)