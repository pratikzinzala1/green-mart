package com.order.greenmart.ui.home.orderlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.order.greenmart.GreenMartApplication
import com.order.greenmart.retrofitdatabase.GreenMartApi
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.DataXXX
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.OrderListResponse
import retrofit2.Call
import retrofit2.Response

class OrderlistViewModel : ViewModel() {

    private var _orderlist = MutableLiveData<MutableList<DataXXX>>()
    val orderlist: LiveData<MutableList<DataXXX>>
        get() = _orderlist
    private var _progressnotifier = MutableLiveData<Int>(0)
    val progressnotifier: LiveData<Int> get() = _progressnotifier


    fun reFreshOrderList() {
//        viewModelScope.launch(Dispatchers.IO) {
        if (GreenMartApplication.sharedPreferences!!.contains("JWTTOKEN")) {
            val x =
                GreenMartApplication.sharedPreferences!!.getString("JWTTOKEN", null).toString()
            getCartData(x)
        }
//        }
    }


    fun getCartData(z: String) {
        // withContext(Dispatchers.Main) {
        _progressnotifier.value = 1
        // }
        Log.d("GETJWT", "HOME $z")

        val productreq = GreenMartApi.retrofitService.getOrderList("Bearer $z")

        productreq.enqueue(object : retrofit2.Callback<OrderListResponse> {
            var productdata: List<DataXXX> = emptyList()
            override fun onResponse(
                call: Call<OrderListResponse>,
                response: Response<OrderListResponse>
            ) {

                if (response.code() == 200) {
                    println("This is order list")
                    productdata = response.body()!!.data!!

                    _orderlist.value = productdata.toMutableList()
                    if (response.body()!!.data.isNullOrEmpty()
                    ) {
                        _progressnotifier.value = 2

                    } else {
                        _progressnotifier.value = 3
                    }

                } else {

                    println(response.code())
                }


            }

            override fun onFailure(call: Call<OrderListResponse>, t: Throwable) {


            }
        })

    }

}