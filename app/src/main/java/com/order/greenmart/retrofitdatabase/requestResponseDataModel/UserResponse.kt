package com.order.greenmart.retrofitdatabase.requestResponseDataModel

data class UserResponse(
    val data: UserData?,
    val msg: String,
    val status: Int
)