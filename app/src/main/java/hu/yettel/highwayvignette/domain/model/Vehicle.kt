package hu.yettel.highwayvignette.domain.model

data class Vehicle(
    val plate: String,
    val ownerName: String,
    val vehicleType: String,
    val country: LocalizedText,
    val currentVignetteType: String
)