package com.order.greenmart.retrofitdatabase.requestmodel

data class PasswordRequest(
    val confirmPass: String,
    val newPass: String
)