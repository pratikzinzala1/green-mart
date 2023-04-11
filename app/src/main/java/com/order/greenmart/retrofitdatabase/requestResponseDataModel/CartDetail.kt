package com.order.greenmart.retrofitdatabase.requestResponseDataModel

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.order.greenmart.RemoveFromCart
import com.order.greenmart.RemoveFromWishList
import com.order.greenmart.adapter.CartAdapter
import com.order.greenmart.adapter.WishListAdapter
import com.order.greenmart.retrofitdatabase.requestmodel.CartItemRequest
import com.order.greenmart.retrofitdatabase.requestmodel.WishListRemoveRequest
import com.order.greenmart.ui.home.cart.CartViewModel

data class CartDetail(
    var _id: String?,
    val cartId: String?,
    var itemTotal: Double?,
    val productDetails: CartProductDetail,
    var quantity: Int,
    val userId: String
) {

    fun increaseItem(
        life: LifecycleOwner,
        viewModel: CartViewModel,
        productItemCount: TextView,
        totalperitem: TextView
    ) {

        quantity++
        productItemCount.text = quantity.toString()
        itemTotal = productDetails.price.toDouble().times(quantity)
        totalperitem.text = "$" + itemTotal.toString()
        viewModel.updateCartTotal(productDetails.price.toDouble())

        val obj = CartItemRequest(_id!!)
        viewModel.increaseCartItem(obj, quantity)


    }
    fun decreaseItem(
        life: LifecycleOwner,
        viewModel: CartViewModel,
        productItemCount: TextView,
        totalperitem: TextView
    ) {

        if (quantity>0){
            quantity--
            productItemCount.text = quantity.toString()
            itemTotal = productDetails.price.toDouble().times(quantity)
            totalperitem.text = "$" + itemTotal.toString()
            viewModel.updateCartTotal(productDetails.price.toDouble())

            val obj = CartItemRequest(_id!!)
            viewModel.decreaseCartItem(obj, quantity)

        }

    }

    fun removeFromCart(

        adapter: CartAdapter,
        position: Int,

    ) {

        val obj = CartItemRequest(_id!!)
        removeItem(position, adapter)
        RemoveFromCart(obj)

    }

    fun removeItem(position: Int, adapter: CartAdapter) {
        val currentList = adapter.currentList.toMutableList()

        try {
            currentList.removeAt(position)
            println("This is  = " + currentList.size)
            adapter.submitList(currentList)
        } catch (e: ArrayIndexOutOfBoundsException) {
            println(e)
        }


    }


}