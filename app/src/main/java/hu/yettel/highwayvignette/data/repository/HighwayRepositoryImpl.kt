package hu.yettel.highwayvignette.data.repository

import hu.yettel.highwayvignette.data.remote.HighwayApi
import hu.yettel.highwayvignette.data.remote.mapper.toDomain
import hu.yettel.highwayvignette.domain.model.Vehicle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HighwayRepositoryImpl @Inject constructor(
    private val api: HighwayApi
) : HighwayRepository {

    override suspend fun getVehicle(): Vehicle = api.getVehicleInfo().toDomain()
}