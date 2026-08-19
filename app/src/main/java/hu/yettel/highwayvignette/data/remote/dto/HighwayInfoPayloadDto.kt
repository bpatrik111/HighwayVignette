package hu.yettel.highwayvignette.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class HighwayInfoPayloadDto(
    val highwayVignettes: List<HighwayVignetteDto>,
    val vehicleCategories: List<VehicleCategoryDto>,
    val counties: List<CountyDto>
)