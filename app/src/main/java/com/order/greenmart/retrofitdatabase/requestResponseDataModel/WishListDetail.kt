package com.order.greenmart.retrofitdatabase.requestResponseDataModel

import android.widget.Button
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.order.greenmart.RemoveFromWishList
import com.order.greenmart.adapter.WishListAdapter
import com.order.greenmart.retrofitdatabase.requestmodel.WishListRemoveRequest

data class WishListDetail(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val productDetails: WishListProductDetail,
    val updatedAt: String,
    val userId: String
) {
    var isInWatchList: Boolean? = false


    fun addToWishList(adapter: WishListAdapter, position: Int) {

        if (isInWatchList!! == false) {
            isInWatchList = true
            removeItem(position, adapter)
            val obj = WishListRemoveRequest(_id)
            RemoveFromWishList(obj)


        }


    }

    fun removeItem(position: Int, adapter: WishListAdapter) {
        val currentList = adapter.currentList.toMutableList()
        try {
            currentList.removeAt(position)
            adapter.submitList(currentList)

        } catch (e: ArrayIndexOutOfBoundsException) {
            println(e)
        }


    }

}