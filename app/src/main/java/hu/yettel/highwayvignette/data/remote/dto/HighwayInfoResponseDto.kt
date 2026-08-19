package hu.yettel.highwayvignette.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class HighwayInfoResponseDto(
    val requestId: Long,
    val statusCode: String,
    val payload: HighwayInfoPayloadDto,
    val dataType: String? = null
)