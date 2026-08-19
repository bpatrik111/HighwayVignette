package hu.yettel.highwayvignette.domain.model

data class OrderLineItem(
    val type: String,
    val category: String,
    val cost: Double
)