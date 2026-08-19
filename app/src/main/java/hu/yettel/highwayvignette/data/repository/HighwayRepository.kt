package hu.yettel.highwayvignette.data.repository

import hu.yettel.highwayvignette.domain.model.Vehicle
import hu.yettel.highwayvignette.domain.model.VignetteOption

interface HighwayRepository {
    suspend fun getVehicle(): Vehicle
    suspend fun getNationalVignetteOptions(): List<VignetteOption>
}