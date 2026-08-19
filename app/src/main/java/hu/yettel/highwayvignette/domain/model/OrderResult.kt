package hu.yettel.highwayvignette.domain.model

sealed interface OrderResult {
    data class Success(val requestId: Long, val receivedOrders: List<OrderLineItem>) : OrderResult
    data class Failure(val message: String) : OrderResult
}