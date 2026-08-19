package hu.yettel.highwayvignette.data.remote

import hu.yettel.highwayvignette.data.remote.dto.HighwayInfoResponseDto
import hu.yettel.highwayvignette.data.remote.dto.HighwayOrderRequestDto
import hu.yettel.highwayvignette.data.remote.dto.HighwayOrderResponseDto
import hu.yettel.highwayvignette.data.remote.dto.VehicleResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HighwayApi {

    @GET("v1/highway/info")
    suspend fun getHighwayInfo(): HighwayInfoResponseDto

    @GET("v1/highway/vehicle")
    suspend fun getVehicleInfo(): VehicleResponseDto

    @POST("v1/highway/order")
    suspend fun postOrder(@Body body: HighwayOrderRequestDto): HighwayOrderResponseDto
}