package hu.yettel.highwayvignette.data.repository

import hu.yettel.highwayvignette.domain.model.Vehicle

interface HighwayRepository {
    suspend fun getVehicle(): Vehicle
}