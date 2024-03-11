package com.angus.api_gateway.data.model.restaurant

enum class OrderStatus(val statusCode: Int) {
    PENDING(0),
    APPROVED(1),
    IN_COOKING(2),
    DONE(3),
    CANCELED(4);


    companion object {
        fun getOrderStatus(statusCode: Int): OrderStatus {
            OrderStatus.values().forEach {
                if (it.statusCode == statusCode) {
                    return it
                }
            }
            return PENDING
        }
    }
}