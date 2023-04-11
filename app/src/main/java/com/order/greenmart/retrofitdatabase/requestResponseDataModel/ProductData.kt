package com.order.greenmart.retrofitdatabase.requestResponseDataModel

data class ProductData(
    val data: List<ProductDetails>,
    val msg: String?,
    val staus: Int?,
    val totalProduct: Int?
)