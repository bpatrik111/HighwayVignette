package hu.yettel.highwayvignette.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CountyDto(
    val id: String,
    val name: String
)