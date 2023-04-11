package com.order.greenmart.ui.home.cart

import android.util.Log
import androidx.lifecycle.*
import com.order.greenmart.GreenMartApplication
import com.order.greenmart.retrofitdatabase.GreenMartApi
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.*
import com.order.greenmart.retrofitdatabase.requestmodel.CartItemRequest
import com.order.greenmart.retrofitdatabase.requestmodel.PlaceOrderRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class CartViewModel : ViewModel() {

    private var _cartlist = MutableLiveData<MutableList<CartDetail>>()
    val cartlist: LiveData<MutableList<CartDetail>>
        get() = _cartlist


    private var _cartTotalPrice = MutableLiveData<Double>(0.0)
    val cartTotalPrice: LiveData<Double>
        get() = _cartTotalPrice


    fun updateCartTotal(addedvalue: Double) {
        _cartTotalPrice.value = _cartTotalPrice.value!! + addedvalue
    }

    private var _progressnotifier = MutableLiveData<Int>(0)
    val progressnotifier: LiveData<Int> get() = _progressnotifier


    private var cartid: String? = null


    fun reFreshCart() {

        if (GreenMartApplication.sharedPreferences!!.contains("JWTTOKEN")) {
            val x =
                GreenMartApplication.sharedPreferences!!.getString("JWTTOKEN", null).toString()
            _progressnotifier.value = 1

            getCartData(x)
        }
    }

    fun privateRefreshCart() {
        if (GreenMartApplication.sharedPreferences!!.contains("JWTTOKEN")) {
            val x =
                GreenMartApplication.sharedPreferences!!.getString("JWTTOKEN", null).toString()
            getCartData(x)
            _progressnotifier.value = 3
        }
    }


    fun getCartData(z: String) {
        // withContext(Dispatchers.Main) {

        // }
        Log.d("GETJWT", "HOME $z")

        val productreq = GreenMartApi.retrofitService.getMyCart("Bearer $z")

        productreq.enqueue(object : retrofit2.Callback<CartListResponse> {
            var productdata: List<CartDetail> = emptyList()
            override fun onResponse(
                call: Call<CartListResponse>,
                response: Response<CartListResponse>
            ) {

                if (response.code() == 200) {
                    if (response.body()!!.cartTotal != null && response.body()!!.data != null) {
                        _cartTotalPrice.value = response.body()!!.cartTotal!!
                        productdata = response.body()!!.data!!

                        cartid = response.body()!!.data!![0].cartId

                    }
                    _cartlist.value = productdata.toMutableList()
                    if (response.body()!!.data.isNullOrEmpty()
                    ) {
                        _cartTotalPrice.value = 0.0
                        _progressnotifier.value = 2

                    } else {
                        _progressnotifier.value = 3
                    }
                    Log.d("CART", response.code().toString())
                    Log.d("CART", response.body()!!.data.toString())
                } else {
                    Log.d("CART", response.code().toString())
                    Log.d("CART", response.message())
                }

            }

            override fun onFailure(call: Call<CartListResponse>, t: Throwable) {


            }
        })

    }

    fun decreaseCartItem(cartItemRequest: CartItemRequest, quantityx: Int): LiveData<Int> {
        _progressnotifier.value = 3

        val cartItemRequest = cartItemRequest
        val jwtToken: String =
            GreenMartApplication.sharedPreferences!!.getString("JWTTOKEN", null).toString()


        val requestCall =
            GreenMartApi.retrofitService.decreaseProductQuantity(cartItemRequest, jwtToken)


        val _quantity = MutableLiveData<Int>(quantityx)
        val quantit: LiveData<Int> = _quantity


        val obj = object : retrofit2.Callback<CartItemResponse> {
            override fun onResponse(
                call: Call<CartItemResponse>,
                response: Response<CartItemResponse>
            ) {


                if (response.code() == 200) {

                    println("this is Decrease" + response.body())
                    if (response.body()!!.data == null) {

                        privateRefreshCart()
                    }

                } else {
                    _progressnotifier.value = 3

                    println("NOT Removed")
                    println(response.code())
                    Log.d("ADDED", "NOT increase")
                }

            }

            override fun onFailure(call: Call<CartItemResponse>, t: Throwable) {
                _progressnotifier.value = 3

                println("Wrong")
            }
        }

        requestCall.enqueue(obj)

        return quantit
    }


    fun increaseCartItem(cartItemRequest: CartItemRequest, quantityx: Int): LiveData<Int> {
        _progressnotifier.value = 3

        val cartItemRequest = cartItemRequest
        val jwtToken: String =
            GreenMartApplication.sharedPreferences!!.getString("JWTTOKEN", null).toString()


        val requestCall =
            GreenMartApi.retrofitService.increaseProductQuantity(cartItemRequest, jwtToken)


        val _quantity = MutableLiveData<Int>(quantityx)
        val quantit: LiveData<Int> = _quantity


        val obj = object : retrofit2.Callback<CartItemResponse> {
            override fun onResponse(
                call: Call<CartItemResponse>,
                response: Response<CartItemResponse>
            ) {


                if (response.code() == 200) {
                    //_quantity.value = response.body()!!.data.quantity
                    // privateRefreshCart()
                    println("Item increase")

                } else {
                    println("NOT increase")

                }

            }

            override fun onFailure(call: Call<CartItemResponse>, t: Throwable) {
                println("Wrong")
            }
        }

        requestCall.enqueue(obj)

        return quantit
    }


    fun placeOrder(total: String): LiveData<Boolean> {
        _progressnotifier.value = 1

        val _oncomplete = MutableLiveData<Boolean>(false)
        val oncomplete: LiveData<Boolean> = _oncomplete
        viewModelScope.launch(Dispatchers.IO) {
            val placeOrderRequest = PlaceOrderRequest(cartid!!, total)
            val jwtToken: String =
                GreenMartApplication.sharedPreferences!!.getString("JWTTOKEN", null).toString()


            val requestCall = GreenMartApi.retrofitService.placeOrder(placeOrderRequest, jwtToken)



            requestCall.enqueue(object : retrofit2.Callback<PlaceOrderResponse> {
                override fun onResponse(
                    call: Call<PlaceOrderResponse>,
                    response: Response<PlaceOrderResponse>
                ) {

                    if (response.code() == 201) {
                        _oncomplete.value = true
                        println("Order has been placed")

                        reFreshCart()


                    } else {
                        println("Order not placed")
                    }
                    println(response.code())

                }

                override fun onFailure(call: Call<PlaceOrderResponse>, t: Throwable) {

                }
            })
        }

        return oncomplete
    }


}