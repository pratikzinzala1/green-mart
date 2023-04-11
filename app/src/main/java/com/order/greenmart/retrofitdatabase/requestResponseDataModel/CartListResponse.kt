package com.order.greenmart.retrofitdatabase.requestResponseDataModel

data class CartListResponse(
    val cartTotal: Double?,
    val data: List<CartDetail>?,
    val msg: String,
    val status: Int
)