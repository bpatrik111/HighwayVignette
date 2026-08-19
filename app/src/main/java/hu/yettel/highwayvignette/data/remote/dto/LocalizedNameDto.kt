package hu.yettel.highwayvignette.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LocalizedNameDto(
    val hu: String,
    val en: String
)