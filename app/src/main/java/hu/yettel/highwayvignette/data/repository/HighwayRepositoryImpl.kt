package hu.yettel.highwayvignette.data.repository

import hu.yettel.highwayvignette.data.remote.HighwayApi
import hu.yettel.highwayvignette.data.remote.mapper.toDomain
import hu.yettel.highwayvignette.data.remote.mapper.toDomainOptions
import hu.yettel.highwayvignette.domain.model.Vehicle
import hu.yettel.highwayvignette.domain.model.VignetteOption
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HighwayRepositoryImpl @Inject constructor(
    private val api: HighwayApi
) : HighwayRepository {

    override suspend fun getVehicle(): Vehicle = api.getVehicleInfo().toDomain()

    override suspend fun getNationalVignetteOptions(): List<VignetteOption> {
        val vignettes = api.getHighwayInfo().payload.highwayVignettes
        return vignettes
            .filter { entry -> entry.vignetteType.any { it in NATIONAL_CODES } }
            .flatMap { it.toDomainOptions() }
    }

    companion object {
        private val NATIONAL_CODES = setOf("DAY", "WEEK", "MONTH", "YEAR")
    }
}