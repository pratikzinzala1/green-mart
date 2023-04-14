package com.order.greenmart.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.order.greenmart.GreenMartApplication
import com.order.greenmart.MainActivity
import com.order.greenmart.retrofitdatabase.GreenMartApi
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.*
import com.order.greenmart.retrofitdatabase.requestmodel.CartItemRequest
import com.order.greenmart.retrofitdatabase.requestmodel.PlaceOrderRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class HomeViewModel : ViewModel() {


    private var _productList = MutableLiveData<List<ProductDetails>>()
    val productList: LiveData<List<ProductDetails>>
        get() = _productList

    private var _progressnotifier = MutableLiveData<Int>(0)
    val progressnotifier: LiveData<Int> get() = _progressnotifier

    private var _totalcartquantity = MutableLiveData<Int>(0)
    val totalcartquantity: LiveData<Int> get() = _totalcartquantity

    private var _logout = MutableLiveData<Boolean>(false)
    val logout: LiveData<Boolean> get() = _logout

    private var _cartlist = MutableLiveData<MutableList<CartDetail>>()
    val cartlist: LiveData<MutableList<CartDetail>>
        get() = _cartlist

    private var _cartTotalPrice = MutableLiveData<Double>(0.0)
    val cartTotalPrice: LiveData<Double>
        get() = _cartTotalPrice

    var apiCallCount: Int = 0


    val x = GreenMartApplication.sharedPreferences!!.getString("JWTTOKEN", null).toString()
    fun updateCartTotal(addedvalue: Double) {
        _cartTotalPrice.value = _cartTotalPrice.value!! + addedvalue
    }

    fun updateCartTotaldecrease(removevalue: Double) {
        _cartTotalPrice.value = _cartTotalPrice.value!! - removevalue
    }

    private var cartid: String? = null

    init {
        privateRefreshCart()
        privateReFrashWishList()
    }

    fun getsingleitem(position: Int): ProductDetails {

        var product: ProductDetails = _productList.value!![position]

        _productList.value!!.forEach {
            if (it._id == "f") {
                product = it
            }
        }
        return product
    }


    fun increaseTotal() {
        _totalcartquantity.value = _totalcartquantity.value!! + 1
    }

    fun decreaseTotal() {
        _totalcartquantity.value = _totalcartquantity.value!! - 1
    }

    fun reFreshProductData() {
        _progressnotifier.value = 1
        getProductData()

    }


    fun privateReFreshProductData() {

        _progressnotifier.value = 3

        getProductData()

    }


    fun getProductData() {

        println("Called Get All Data")


        val productreq = GreenMartApi.retrofitService.getAllproduct("Bearer $x")
        viewModelScope.launch(Dispatchers.IO) {
            productreq.enqueue(object : retrofit2.Callback<ProductData> {
                var productdata: List<ProductDetails> = emptyList()
                override fun onResponse(call: Call<ProductData>, response: Response<ProductData>) {

                    if (response.code() == 200) {
                        productdata = response.body()!!.data
                        _productList.value = productdata
                        _totalcartquantity.value = response.body()!!.totalProduct!!
                        if (response.body()!!.data.isEmpty()) {
                            _progressnotifier.value = 2

                        } else {
                            _progressnotifier.value = 3
                        }
                    } else if (response.code() == 500) {

                        _logout.value = true

                    } else {
                        _progressnotifier.value = 3

                        println("Home data error code : ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ProductData>, t: Throwable) {
                    _progressnotifier.value = 3

                    println("Failed to Load Data")
                }
            })
        }


    }


    fun reFreshCart() {
        _progressnotifier.value = 1
        getCartData()
    }

    fun privateRefreshCart() {
        getCartData()
        _progressnotifier.value = 3
    }


    fun getCartData() {

        Log.d("GETJWT", "HOME $x")

        val productreq = GreenMartApi.retrofitService.getMyCart("Bearer $x")

        productreq.enqueue(object : retrofit2.Callback<CartListResponse> {
            var productdata: List<CartDetail> = emptyList()
            override fun onResponse(
                call: Call<CartListResponse>, response: Response<CartListResponse>
            ) {

                if (response.code() == 200) {
                    if (response.body()!!.cartTotal != null && response.body()!!.data != null) {
                        _cartTotalPrice.value = response.body()!!.cartTotal!!
                        productdata = response.body()!!.data!!
                        cartid = response.body()!!.data!![0].cartId

                    }
                    _cartlist.value = productdata.toMutableList()
                    if (response.body()!!.data.isNullOrEmpty()) {
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

    fun decreaseCartItem(cartItemRequest: CartItemRequest) {
        val jwtToken =
            GreenMartApplication.sharedPreferences?.getString("JWTTOKEN", null).toString()
        val requestCall =
            GreenMartApi.retrofitService.decreaseProductQuantity(cartItemRequest, jwtToken)
        val obj = object : retrofit2.Callback<CartItemResponse> {
            override fun onResponse(
                call: Call<CartItemResponse>, response: Response<CartItemResponse>
            ) {
                if (response.code() == 200) {
                    println("item decreased")
                    if (response.body()?.data == null) {
                        privateRefreshCart()
                    }
                } else {
                    println("item not decreased")
                }
            }

            override fun onFailure(call: Call<CartItemResponse>, t: Throwable) {
                println("Wrong")
            }
        }
        requestCall.enqueue(obj)

    }


    fun increaseCartItem(cartItemRequest: CartItemRequest) {
        val jwtToken =
            GreenMartApplication.sharedPreferences?.getString("JWTTOKEN", null).toString()
        val requestCall =
            GreenMartApi.retrofitService.increaseProductQuantity(cartItemRequest, jwtToken)
        val obj = object : retrofit2.Callback<CartItemResponse> {
            override fun onResponse(
                call: Call<CartItemResponse>, response: Response<CartItemResponse>
            ) {
                if (response.code() == 200) {
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
                    call: Call<PlaceOrderResponse>, response: Response<PlaceOrderResponse>
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


    private var _WishList = MutableLiveData<MutableList<WishListDetail>>()
    val WishList: LiveData<MutableList<WishListDetail>>
        get() = _WishList


    fun reFrashWishList() {
        _progressnotifier.value = 1
        getWishListData()
    }
    fun privateReFrashWishList() {
        _progressnotifier.value = 3
        getWishListData()
    }


    fun getWishListData() {


        val productreq = GreenMartApi.retrofitService.getWatchList("Bearer $x")

        viewModelScope.launch(Dispatchers.IO) {

            productreq.enqueue(object : retrofit2.Callback<WishListResponse> {
                var WishListData: MutableList<WishListDetail> = mutableListOf()
                override fun onResponse(
                    call: Call<WishListResponse>, response: Response<WishListResponse>
                ) {
                    if (response.code() == 200) {
                        WishListData = response.body()!!.data!!.toMutableList()
                        _WishList.value = WishListData

                        if (response.body()!!.data!!.isEmpty()) {
                            _progressnotifier.value = 2
                        } else {
                            _progressnotifier.value = 3
                        }

                    }
                }

                override fun onFailure(call: Call<WishListResponse>, t: Throwable) {
                    _progressnotifier.value = 3

                }
            })
        }
    }

}
