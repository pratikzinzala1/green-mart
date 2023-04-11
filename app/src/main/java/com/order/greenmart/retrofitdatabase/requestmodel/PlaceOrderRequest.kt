package com.order.greenmart.retrofitdatabase.requestmodel

data class PlaceOrderRequest(
    val cartId: String,
    val cartTotal: String
)