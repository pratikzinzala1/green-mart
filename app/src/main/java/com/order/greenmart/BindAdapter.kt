package com.order.greenmart

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.order.greenmart.adapter.CartAdapter
import com.order.greenmart.adapter.OrderListAdapter
import com.order.greenmart.adapter.ProductAdapter
import com.order.greenmart.adapter.WishListAdapter
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.CartDetail
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.DataXXX
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.WishListDetail
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.ProductDetails
import java.net.URL


@BindingAdapter("productlist", "CONTEXT")
fun bindWishList(
    recyclerView: RecyclerView,
    productlist: LiveData<MutableList<WishListDetail>>,
    CONTEXT: LifecycleOwner
) {
    val adapter = recyclerView.adapter as WishListAdapter

    productlist.observe(CONTEXT, Observer {
        adapter.submitList(it)
    })

}



@BindingAdapter("ImageUrl","context")
fun bindImage(imgView: ImageView, imgUrl: String,context:Context) {

    try {
        val url = URL(imgUrl)
        Glide.with(context).load(url).into(imgView)
    } catch (e: Exception) {
        println(e)
    }


}



@BindingAdapter("cartlist","CONTEXT")
fun bindCartList(
    recyclerView: RecyclerView,
    cartlist: LiveData<MutableList<CartDetail>>,
    CONTEXT: LifecycleOwner
) {
    val adapter = recyclerView.adapter as CartAdapter


    cartlist.observe(CONTEXT, Observer {
        adapter.submitList(it)
    })

}





@BindingAdapter("productlist","CONTEXT")
fun bindProductList(
    recyclerView: RecyclerView,
    productlist: LiveData<List<ProductDetails>>,
    CONTEXT: LifecycleOwner
) {
    val adapter = recyclerView.adapter as ProductAdapter


    productlist.observe(CONTEXT, Observer {
        adapter.submitList(it)
        Log.d("DATA", "Binding List : " + it.toString())
    })


}
@BindingAdapter("orderlist","CONTEXT")
fun bindOrderList(
    recyclerView: RecyclerView,
    productlist: LiveData<MutableList<DataXXX>>,
    CONTEXT: LifecycleOwner
) {
    val adapter = recyclerView.adapter as OrderListAdapter


    productlist.observe(CONTEXT, Observer {
        adapter.submitList(it)
        Log.d("DATA", "Binding List : " + it.toString())
    })


}


