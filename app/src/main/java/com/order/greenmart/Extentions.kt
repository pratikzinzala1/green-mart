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