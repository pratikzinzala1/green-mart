package com.order.greenmart.retrofitdatabase.requestResponseDataModel

data class WishListResponse(
    val `data`: List<WishListDetail>?,
    val msg: String,
    val status: Int
)