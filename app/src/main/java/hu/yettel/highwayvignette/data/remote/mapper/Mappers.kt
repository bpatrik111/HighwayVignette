package hu.yettel.highwayvignette.data.remote.mapper

import hu.yettel.highwayvignette.data.remote.dto.CountyDto
import hu.yettel.highwayvignette.data.remote.dto.HighwayOrderItemDto
import hu.yettel.highwayvignette.data.remote.dto.HighwayVignetteDto
import hu.yettel.highwayvignette.data.remote.dto.LocalizedNameDto
import hu.yettel.highwayvignette.data.remote.dto.VehicleResponseDto
import hu.yettel.highwayvignette.domain.model.County
import hu.yettel.highwayvignette.domain.model.LocalizedText
import hu.yettel.highwayvignette.domain.model.OrderLineItem
import hu.yettel.highwayvignette.domain.model.Vehicle
import hu.yettel.highwayvignette.domain.model.VignetteOption

fun LocalizedNameDto.toDomain(): LocalizedText = LocalizedText(hu = hu, en = en)

fun VehicleResponseDto.toDomain(): Vehicle = Vehicle(
    plate = plate,
    ownerName = name,
    vehicleType = type,
    country = country.toDomain(),
    currentVignetteType = vignetteType
)

fun CountyDto.toDomain(): County = County(id = id, name = name)

fun HighwayOrderItemDto.toDomain(): OrderLineItem = OrderLineItem(type = type, category = category, cost = cost)

fun OrderLineItem.toDto(): HighwayOrderItemDto = HighwayOrderItemDto(type = type, category = category, cost = cost)


fun HighwayVignetteDto.toDomainOptions(): List<VignetteOption> =
    vignetteType.map { code ->
        VignetteOption(
            id = code,
            vehicleCategory = vehicleCategory,
            cost = cost,
            trxFee = trxFee,
            sum = sum
        )
    }