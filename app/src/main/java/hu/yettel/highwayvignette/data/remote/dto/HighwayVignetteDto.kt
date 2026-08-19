package hu.yettel.highwayvignette.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class HighwayVignetteDto(
    val vignetteType: List<String>,
    val vehicleCategory: String,
    val cost: Double,
    val trxFee: Double,
    val sum: Double
)