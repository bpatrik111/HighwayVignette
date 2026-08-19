package hu.yettel.highwayvignette.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class VehicleResponseDto(
    val requestId: Long,
    val statusCode: String,
    val internationalRegistrationCode: String,
    val type: String,
    val name: String,
    val plate: String,
    val country: LocalizedNameDto,
    val vignetteType: String
)