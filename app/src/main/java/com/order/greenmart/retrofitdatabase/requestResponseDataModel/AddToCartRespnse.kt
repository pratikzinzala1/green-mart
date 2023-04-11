package com.order.greenmart.retrofitdatabase.requestResponseDataModel

data class AddToCartRespnse(
    val `data`: AddToCartDetail?,
    val msg: String,
    val status: Int
)