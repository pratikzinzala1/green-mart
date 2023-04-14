package com.order.greenmart

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import com.order.greenmart.retrofitdatabase.GreenMartApi
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.*
import com.order.greenmart.retrofitdatabase.requestmodel.AddToCartRequest
import com.order.greenmart.retrofitdatabase.requestmodel.CartItemRequest
import com.order.greenmart.retrofitdatabase.requestmodel.WishListRemoveRequest
import retrofit2.Call
import retrofit2.Response
import java.util.regex.Matcher
import java.util.regex.Pattern



fun RemoveFromCart(cartItemRequest: CartItemRequest): LiveData<Boolean> {
    val addToCartRequest = cartItemRequest
    val jwtToken: String =
        GreenMartApplication.sharedPreferences!!.getString("JWTTOKEN", null).toString()


    val requestCall = GreenMartApi.retrofitService.removeFromCart(addToCartRequest, jwtToken)

    val _oncomplete = MutableLiveData<Boolean>()
    val oncomplete: LiveData<Boolean> = _oncomplete

    requestCall.enqueue(object : retrofit2.Callback<AddToCartRespnse> {
        override fun onResponse(
            call: Call<AddToCartRespnse>,
            response: Response<AddToCartRespnse>
        ) {

            if (response.code() == 200) {
                _oncomplete.value = true

                println("Removed")
                Log.d("ADDED", "Removed")
            } else {
                println("NOT ADDED")
                println(response.code())
                Log.d("ADDED", "NOT Removed")
            }

        }

        override fun onFailure(call: Call<AddToCartRespnse>, t: Throwable) {
            println("Wrong")
            Log.d("ADDED", "Wrong")
        }
    })
    return oncomplete
}


fun AddToWishList(addToCartRequest: AddToCartRequest): LiveData<String> {
    val addToCartRequest = addToCartRequest
    val jwtToken: String = GreenMartApplication.sharedPreferences!!.getString("JWTTOKEN", null).toString()


    val requestCall = GreenMartApi.retrofitService.AddToWishList(addToCartRequest, jwtToken)

    val _wishitemid = MutableLiveData<String>()
    val wishitemid: LiveData<String> = _wishitemid

    requestCall.enqueue(object : retrofit2.Callback<AddToWatchListResponse> {
        override fun onResponse(
            call: Call<AddToWatchListResponse>,
            response: Response<AddToWatchListResponse>
        ) {

            if (response.code() == 200) {
                _wishitemid.value = response.body()!!.data._id
                println("ItemAdded to WishList")
            } else {
                println("NOT ADDED")
                println(response.code())
            }

        }

        override fun onFailure(call: Call<AddToWatchListResponse>, t: Throwable) {
            println("Wrong")
        }

    })


    return wishitemid
}


fun RemoveFromWishList(wishListRemoveRequest: WishListRemoveRequest):LiveData<Boolean>  {
    val wishListRemoveRequest = wishListRemoveRequest
    val jwtToken: String =
        GreenMartApplication.sharedPreferences!!.getString("JWTTOKEN", null).toString()
    val requestCall =
        GreenMartApi.retrofitService.removeFromWishList(wishListRemoveRequest, jwtToken)



    val _oncomplete = MutableLiveData<Boolean>()
    val oncomplete: LiveData<Boolean> = _oncomplete

    requestCall.enqueue(object : retrofit2.Callback<WishListResponse> {
        override fun onResponse(
            call: Call<WishListResponse>,
            response: Response<WishListResponse>
        ) {

            if (response.code() == 200) {

                _oncomplete.value = true
                println("Item Removed from WishList")
            } else {
                println("NOT Removed")
                println(response.code())
            }
        }

        override fun onFailure(call: Call<WishListResponse>, t: Throwable) {
            println("Wrong")
        }
    })


    return oncomplete
}





fun AddToCart(addToCartRequest: AddToCartRequest): LiveData<AddToCartDetail> {
    val addToCartRequest = addToCartRequest
    val jwtToken: String =
        GreenMartApplication.sharedPreferences!!.getString("JWTTOKEN", null).toString()
    val requestCall = GreenMartApi.retrofitService.AddToCart(addToCartRequest, jwtToken)

    var _liveResponse = MutableLiveData<AddToCartDetail>()
    val liveResponse: LiveData<AddToCartDetail> = _liveResponse



    requestCall.enqueue(object : retrofit2.Callback<AddToCartRespnse> {
        override fun onResponse(
            call: Call<AddToCartRespnse>,
            response: Response<AddToCartRespnse>
        ) {

            if (response.code() == 201) {

                _liveResponse.value = response.body()!!.data!!

                println("ItemAdded")
                Log.d("ADDED", "ItemAdded")
            } else {
                println("NOT ADDED")
                println(response.code())
                Log.d("ADDED", "NOT ADDED")
            }

        }

        override fun onFailure(call: Call<AddToCartRespnse>, t: Throwable) {
            println("Wrong")
            Log.d("ADDED", "Wrong")
        }
    })

    return liveResponse
}


fun Context.somethingwentwrong() {

    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
}

fun TextInputLayout.removeErrorAfterTextChange() {

    this.editText!!.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            this@removeErrorAfterTextChange.error = null
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    })
}

fun String.isValidEmail(): Boolean {
    val pattern = Patterns.EMAIL_ADDRESS
    return pattern.matcher(this).matches()
}

fun String.isValidPhone(): Boolean {

    val pattern = Patterns.PHONE
    if (!pattern.matcher(this).matches()) {
        return false
    }


    if (this.length != 10) {

        return false

    }
    return true
}

fun String.isValidName(): Boolean {

    val NAME_REGEX = "^[A-Za-z ]*$"


    var p: Pattern = Pattern.compile(NAME_REGEX)
    var m: Matcher = p.matcher(this)
    return m.matches()
}