package hu.yettel.highwayvignette.domain.model

data class VignetteOption(
    val id: String,
    val vehicleCategory: String,
    val cost: Double,
    val trxFee: Double,
    val sum: Double
)