package hu.yettel.highwayvignette.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class HighwayOrderItemDto(
    val type: String,
    val category: String,
    val cost: Double
)