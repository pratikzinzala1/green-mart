package com.order.greenmart.retrofitdatabase.requestResponseDataModel

import android.content.Context

import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
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

    fun addTocart(
        life: LifecycleOwner,
        view: TextView,
        context: Context,
        viewModel: HomeViewModel
    ) {


        println(viewModel.apiCallCount)
        if (cartItemId != null) {
            view.isEnabled = false
            viewModel.apiCallCount++
            val obj = CartItemRequest(cartItemId!!)
            val live = viewModel.RemoveFromCart(obj)

            view.text = "Wait...."

            live.observe(life, Observer {
                viewModel.apiCallCount--
                view.isEnabled = true
                viewModel.decreaseTotal()
                if (viewModel.apiCallCount == 0) {
                    view.text = "Add To Cart"
                    viewModel.privateReFreshProductData()
                }

            })

            cartItemId = null
            quantity = null


            view.setTextColor(context.getColor(R.color.basilgreen800))
            view.background =
                context.resources.getDrawable(R.drawable.leftroundborder, context.theme)


        } else if (view.isEnabled == true) {

            view.isEnabled = false
            viewModel.apiCallCount++
            val obj = AddToCartRequest(_id)
            val live = viewModel.AddToCart(obj)
            view.text = "Wait...."

            live.observe(life, Observer {
                view.isEnabled = true
                viewModel.apiCallCount--
                viewModel.increaseTotal()
                cartItemId = it.productDetails._id
                quantity = it.quantity
                view.text = "Remove From Cart"

                if (viewModel.apiCallCount == 0) {
                    viewModel.privateReFreshProductData()

                }
            })
            cartItemId = "0"
            quantity = 0

            view.setTextColor(context.getColor(R.color.white))
            view.background =
                context.resources.getDrawable(R.drawable.leftroundfilled, context.theme)


        }

    }


    fun addToWishList(life: LifecycleOwner, view: TextView, context: Context,viewModel: HomeViewModel) {
        if (watchListItemId != null) {
            view.isEnabled = false
            viewModel.apiCallCount++

            val obj = WishListRemoveRequest(watchListItemId!!)
            val live = viewModel.RemoveFromWishList(obj)
            view.text = "Wait...."

            live.observe(life, Observer {
                //privateReFreshProductData()
                view.isEnabled = true
                viewModel.apiCallCount--
                view.text = "Add To Wishlist"

                if (viewModel.apiCallCount == 0) {
                    viewModel.privateReFreshProductData()
                }
            })
            watchListItemId = null

            view.setTextColor(context.getColor(R.color.basilgreen800))
            view.background =
                context.resources.getDrawable(R.drawable.rightroundborder, context.theme)


        } else if (view.isEnabled == true) {
            viewModel.apiCallCount++
            view.isEnabled = false
            val obj = AddToCartRequest(_id)
            val live = viewModel.AddToWishList(obj)
            view.text = "Wait...."

            live.observe(life, Observer {
                watchListItemId = it
                view.isEnabled = true
                view.text = "Remove From Wishlist"

                viewModel.apiCallCount--
                if (viewModel.apiCallCount == 0) {
                    viewModel.privateReFreshProductData()
                }

            })
            watchListItemId = "0"

            view.setTextColor(context.getColor(R.color.white))
            view.background =
                context.resources.getDrawable(R.drawable.rightroundfilled, context.theme)

        }
    }




}