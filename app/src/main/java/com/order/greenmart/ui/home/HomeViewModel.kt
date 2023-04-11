package com.order.greenmart.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.order.greenmart.GreenMartApplication
import com.order.greenmart.MainActivity
import com.order.greenmart.retrofitdatabase.GreenMartApi
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.*
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


    fun getsingleitem(position:Int):ProductDetails{
       var product:ProductDetails = _productList.value!![position]

        _productList.value!!.forEach {
            if (it._id == "f"){
                product = it
            }
        }
        return product
    }


    fun increaseTotal(){
        _totalcartquantity.value = _totalcartquantity.value!! + 1
    }
    fun decreaseTotal(){
        _totalcartquantity.value = _totalcartquantity.value!! - 1
    }
    fun reFreshProductData() {
        _progressnotifier.value = 1

        if (GreenMartApplication.sharedPreferences!!.contains("JWTTOKEN")) {
            val x =
                GreenMartApplication.sharedPreferences!!.getString("JWTTOKEN", null).toString()
            getProductData(x)
        }

    }


    fun privateReFreshProductData() {

        if (GreenMartApplication.sharedPreferences!!.contains("JWTTOKEN")) {
            val x =
                GreenMartApplication.sharedPreferences!!.getString("JWTTOKEN", null).toString()
            getProductData(x)
        }
    }


    fun getProductData(z: String) {


        Log.d("GETJWT", "HOME $z")

        val productreq = GreenMartApi.retrofitService.getAllproduct("Bearer $z")
        viewModelScope.launch(Dispatchers.IO) {
            productreq.enqueue(object : retrofit2.Callback<ProductData> {
                var productdata: List<ProductDetails> = emptyList()
                override fun onResponse(call: Call<ProductData>, response: Response<ProductData>) {

                    if (response.code() == 200) {
                        productdata = response.body()!!.data
                        _productList.value = productdata
                        _totalcartquantity.value = response.body()!!.totalProduct!!
                        if (response.body()!!.data.isEmpty()
                        ) {
                            _progressnotifier.value = 2

                        } else {
                            _progressnotifier.value = 3
                        }
                    }else if (response.code() == 500){

                        _logout.value = true

                    }

                    else {
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


}
