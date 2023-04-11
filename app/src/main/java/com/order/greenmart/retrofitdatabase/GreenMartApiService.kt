package com.order.greenmart.retrofitdatabase

import com.order.greenmart.retrofitdatabase.requestResponseDataModel.*
import com.order.greenmart.retrofitdatabase.requestmodel.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

private const val BASE_URL = "https://shopping-app-backend-t4ay.onrender.com/"


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface GreenMartApiService {

    @POST("user/registerUser")
    fun registeruser(@Body registerUserRequest: RegisterUserRequest): Call<UserResponse>

    @POST("user/verifyOtpOnRegister")
    fun verifyOtp(@Body otpRequest: OtpRequest): Call<UserResponse>

    @POST("user/resendOtp")
    fun resendOtp(@Body otpRequest: OtpRequest): Call<UserResponse>

    @POST("user/login")
    fun loginUser(@Body registerUserRequest: RegisterUserRequest): Call<UserResponse>

    @POST("user/forgotPassword")
    fun forgotPassword(@Body registerUserRequest: RegisterUserRequest): Call<UserResponse>

    @POST("user/verifyOtpOnForgotPassword")
    fun verifyOtpOnForgot(@Body otpRequest: OtpRequest): Call<UserResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("user/changePassword")
    fun changPassword(@Body passwordRequest: PasswordRequest, @Header("Authorization") jwtToken:String): Call<UserResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("product/getAllProduct")
    fun getAllproduct(@Header("Authorization") jwtToken:String):Call<ProductData>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("cart/AddToCart")
    fun AddToCart(@Body addToCartRequest: AddToCartRequest, @Header("Authorization") jwtToken:String):Call<AddToCartRespnse>


    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/cart/removeProductFromCart")
    fun removeFromCart(@Body cartItemRequest: CartItemRequest,@Header("Authorization") jwtToken:String):Call<AddToCartRespnse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("watchList/addToWatchList")
    fun AddToWishList(@Body addToCartRequest: AddToCartRequest, @Header("Authorization") jwtToken:String):Call<AddToWatchListResponse>


    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/watchList/getWatchList")
    fun getWatchList(@Header("Authorization") jwtToken:String):Call<WishListResponse>


    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/watchList/removeFromWatchList")
    fun removeFromWishList(@Body wishListRemoveRequest: WishListRemoveRequest,@Header("Authorization") jwtToken:String):Call<WishListResponse>


    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/cart/getMyCart")
    fun getMyCart(@Header("Authorization") jwtToken:String):Call<CartListResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/cart/increaseProductQuantity")
    fun increaseProductQuantity(@Body cartItemRequest: CartItemRequest,@Header("Authorization") jwtToken:String):Call<CartItemResponse>


    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/cart/decreaseProductQuantity")
    fun decreaseProductQuantity(@Body cartItemRequest: CartItemRequest,@Header("Authorization") jwtToken:String):Call<CartItemResponse>



    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/order/placeOrder")
    fun placeOrder(@Body placeOrderRequest: PlaceOrderRequest,@Header("Authorization") jwtToken:String):Call<PlaceOrderResponse>


    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/order/getOrderHistory")
    fun getOrderList(@Header("Authorization") jwtToken:String):Call<OrderListResponse>



}


object GreenMartApi {
    val retrofitService: GreenMartApiService by lazy { retrofit.create(GreenMartApiService::class.java) }
}
