package hu.yettel.highwayvignette.data.repository

import hu.yettel.highwayvignette.domain.model.County
import hu.yettel.highwayvignette.domain.model.OrderLineItem
import hu.yettel.highwayvignette.domain.model.OrderResult
import hu.yettel.highwayvignette.domain.model.Vehicle
import hu.yettel.highwayvignette.domain.model.VignetteOption

interface HighwayRepository {
    suspend fun getVehicle(): Vehicle
    suspend fun getNationalVignetteOptions(): List<VignetteOption>
    suspend fun placeOrder(items: List<OrderLineItem>): OrderResult
    suspend fun getCounties(): List<County>

    suspend fun getCountyVignettePrice(): VignetteOption
}