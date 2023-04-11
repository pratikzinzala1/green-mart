package com.order.greenmart.retrofitdatabase.requestResponseDataModel

data class OrderListResponse(
    val `data`: List<DataXXX>?,
    val msg: String,
    val status: Int
)