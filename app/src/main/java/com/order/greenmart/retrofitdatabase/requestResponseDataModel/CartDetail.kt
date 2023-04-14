package com.order.greenmart.retrofitdatabase.requestResponseDataModel

import android.widget.TextView
import com.order.greenmart.RemoveFromCart
import com.order.greenmart.adapter.CartAdapter
import com.order.greenmart.retrofitdatabase.requestmodel.CartItemRequest
import com.order.greenmart.ui.home.HomeViewModel

data class CartDetail(
    var _id: String?,
    val cartId: String?,
    var itemTotal: Double?,
    val productDetails: CartProductDetail,
    var quantity: Int,
    val userId: String
) {


    fun increaseItem(
        viewModel: HomeViewModel, productItemCount: TextView, totalperitem: TextView
    ) {
        quantity++
        productItemCount.text = quantity.toString()
        itemTotal = productDetails.price.toDouble().times(quantity)
        totalperitem.text = "$" + String.format("%.2f", itemTotal!!.toDouble())
        viewModel.updateCartTotal(productDetails.price.toDouble())
        val obj = CartItemRequest(_id!!)
        viewModel.increaseCartItem(obj)
    }

    fun decreaseItem(
        viewModel: HomeViewModel, productItemCount: TextView, totalperitem: TextView
    ) {

        if (quantity > 0) {
            quantity--
            productItemCount.text = quantity.toString()
            itemTotal = productDetails.price.toDouble().times(quantity)
            totalperitem.text = "$" + String.format("%.2f", itemTotal!!.toDouble())
            viewModel.updateCartTotaldecrease(productDetails.price.toDouble())
            val obj = CartItemRequest(_id!!)
            viewModel.decreaseCartItem(obj)

        }

    }

    fun removeFromCart(
        viewModel: HomeViewModel,
        adapter: CartAdapter,
        position: Int,

        ) {
        viewModel.updateCartTotaldecrease(itemTotal!!)
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