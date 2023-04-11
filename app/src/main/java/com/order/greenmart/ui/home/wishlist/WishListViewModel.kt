package com.order.greenmart.ui.home.wishlist

import android.util.Log
import androidx.lifecycle.*
import com.order.greenmart.GreenMartApplication
import com.order.greenmart.retrofitdatabase.GreenMartApi
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.WishListDetail
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.WishListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class WishListViewModel() : ViewModel() {


    private var _WishList = MutableLiveData<MutableList<WishListDetail>>()
    val WishList: LiveData<MutableList<WishListDetail>>
        get() = _WishList

    private var _progressnotifier = MutableLiveData<Int>(0)
    val progressnotifier: LiveData<Int> get() = _progressnotifier

    fun reFrashWishList() {
        viewModelScope.launch(Dispatchers.IO) {
            if (GreenMartApplication.sharedPreferences!!.contains("JWTTOKEN")) {
                val x =
                    GreenMartApplication.sharedPreferences!!.getString("JWTTOKEN", null).toString()
                getWishListData(x)
            }
        }
    }


    suspend fun getWishListData(z: String) {


        val productreq = GreenMartApi.retrofitService.getWatchList("Bearer $z")
        withContext(Dispatchers.Main) {
            _progressnotifier.value = 1
        }

        productreq.enqueue(object : retrofit2.Callback<WishListResponse> {
            var WishListData: MutableList<WishListDetail> = mutableListOf()
            override fun onResponse(
                call: Call<WishListResponse>,
                response: Response<WishListResponse>
            ) {
                if (response.code() == 200) {
                    Log.d("DATA", response.code().toString())
                    Log.d("DATA", response.body()!!.data.toString())
                    WishListData = response.body()!!.data!!.toMutableList()
                    _WishList.value = WishListData

                    if (response.body()!!.data!!.isEmpty()
                    ) {
                            _progressnotifier.value = 2

                    }else{
                        _progressnotifier.value = 3
                    }

                } else {
                    Log.d("DATA", response.code().toString())
                    Log.d("DATA", response.message())
                }
            }

            override fun onFailure(call: Call<WishListResponse>, t: Throwable) {


            }
        })

    }


}
