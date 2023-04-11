package com.order.greenmart.retrofitdatabase.requestResponseDataModel

data class AddToCartDetail(
    val cartId: String,
    val productCount: Int,
    val productDetails: AddToCartProductDetail,
    val quantity: Int,
    val userId: String
)