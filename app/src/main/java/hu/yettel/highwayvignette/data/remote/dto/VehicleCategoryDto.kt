package hu.yettel.highwayvignette.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class VehicleCategoryDto(
    val category: String,
    val vignetteCategory: String,
    val name: LocalizedNameDto
)