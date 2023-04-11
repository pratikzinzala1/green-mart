package com.order.greenmart.retrofitdatabase.requestResponseDataModel

import android.content.Context
import android.content.res.Resources.Theme
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.order.greenmart.*
import com.order.greenmart.retrofitdatabase.requestmodel.AddToCartRequest
import com.order.greenmart.retrofitdatabase.requestmodel.CartItemRequest
import com.order.greenmart.retrofitdatabase.requestmodel.WishListRemoveRequest
import com.order.greenmart.ui.home.HomeViewModel


data class ProductDetails(
    val __v: Int?,
    val _id: String,
    val createdAt: String?,
    val description: String,
    val imageUrl: String,
    val price: String,
    val title: String,
    val updatedAt: String?,
    var cartItemId: String?,
    var quantity: Int?,
    var watchListItemId: String?
) {


    var isInWatchList: Boolean? = false
    var isInCartList: Boolean? = false

    fun addTocart(life:LifecycleOwner,view:TextView,context: Context,viewModel: HomeViewModel){

        if (cartItemId != null) {

            if (isInCartList!! == false){
                isInCartList = true
                val obj = CartItemRequest(cartItemId!!)
                val live = RemoveFromCart(obj)

                live.observe(life, Observer {

                    viewModel.decreaseTotal()
                    viewModel.privateReFreshProductData()
                    isInCartList = false

                })

                cartItemId = null
                quantity = null
                view.text = "Add To Cart"

                view.setTextColor(context.getColor(R.color.basilgreen800))
                view.background = context.resources.getDrawable(R.drawable.leftroundborder,context.theme)
                // view.setImageResource(R.drawable.ic_cart_border)
            }


        } else if (isInCartList!! == false) {

            isInCartList = true
            val obj = AddToCartRequest(_id)
            val live = AddToCart(obj)
            live.observe(life, Observer {

                isInCartList = false
                viewModel.increaseTotal()
                cartItemId = it.productDetails._id
                quantity = it.quantity
                viewModel.privateReFreshProductData()
            })
            cartItemId = "0"
            quantity = 0
            view.text = "Remove From Cart"

            view.setTextColor(context.getColor(R.color.white))
            view.background = context.resources.getDrawable(R.drawable.leftroundfilled,context.theme)


        }

    }


    fun addToWishList(life:LifecycleOwner,view:TextView,context: Context){
        if (watchListItemId != null) {
            if (isInWatchList!! == false){
                isInWatchList = true
                println("RemoveFromWishList called")
                val obj = WishListRemoveRequest(watchListItemId!!)
                val live = RemoveFromWishList(obj)
                live.observe(life, Observer {
                    //privateReFreshProductData()
                    isInWatchList = false
                })
                watchListItemId = null
                view.text = "Add To Wishlist"

                // view.setImageResource(R.drawable.svg_heart_outline)
                view.setTextColor(context.getColor(R.color.basilgreen800))
                view.background = context.resources.getDrawable(R.drawable.rightroundborder,context.theme)
            }

        } else if (isInWatchList!! == false) {
            isInWatchList = true
            val obj = AddToCartRequest(_id)
            val live = AddToWishList(obj)

            live.observe(life, Observer {
                println(it)
                watchListItemId = it
                isInWatchList = false


                //viewModel.privateReFreshProductData()

            })
            view.text = "Remove From Wishlist"
            watchListItemId = "0"

            view.setTextColor(context.getColor(R.color.white))
            view.background = context.resources.getDrawable(R.drawable.rightroundfilled,context.theme)
           // view.setImageResource(R.drawable.ic_like_filled)

        }
    }

}