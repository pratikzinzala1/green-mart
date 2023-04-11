package com.order.greenmart.retrofitdatabase.requestmodel

data class RegisterUserRequest(
    val emailId: String,
    val mobileNo: String?,
    val name: String?,
    val password: String?
)