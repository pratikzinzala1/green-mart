package com.order.greenmart.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.order.greenmart.R
import com.order.greenmart.RemoveFromWishList

import com.order.greenmart.databinding.ItemWishBinding
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.WishListDetail
import com.order.greenmart.retrofitdatabase.requestmodel.WishListRemoveRequest
import com.order.greenmart.ui.home.DetailProductActivity
import com.order.greenmart.ui.home.wishlist.WishListViewModel

class WishListAdapter(val lifecycleOwner: LifecycleOwner, var viewModel: WishListViewModel) :
    ListAdapter<WishListDetail, WishListAdapter.ItemViewholder>(DiffCallbackWish()) {


    inner class ItemViewholder(private var binding: ItemWishBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WishListDetail) {

            binding.wishresponse = item.productDetails



//            binding.root.setOnClickListener {
//                val i = Intent(context, DetailProductActivity::class.java)
//                i.apply {
//                    this.putExtra("TITLE", item.productDetails.title)
//                    this.putExtra("IMAGEURL", item.productDetails.imageUrl)
//                    this.putExtra("DESCRIPTION", item.productDetails.description)
//                    this.putExtra("_ID", item.productDetails._id)
//                    this.putExtra("CARTITEMID", item.cartItemId)
//                    this.putExtra("PRICE", productModel.price)
//                    this.putExtra("QUANTITY", productModel.quantity)
//                    this.putExtra("WATCHLISTITEMID", productModel.watchListItemId)
//
//                }
//                context.startActivity(i)
//
//            }


            binding.removeFromWishlist.setOnClickListener {


                item.addToWishList(
                    this@WishListAdapter,
                    adapterPosition
                )

            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(ItemWishBinding.inflate(LayoutInflater.from(parent.context)))

    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {

        holder.bind(getItem(position))

    }

    override fun onCurrentListChanged(
        previousList: MutableList<WishListDetail>,
        currentList: MutableList<WishListDetail>
    ) {
        super.onCurrentListChanged(previousList, currentList)

        if (previousList.size ==1 && currentList.isEmpty()){
            viewModel.reFrashWishList()
        }
    }


}

class DiffCallbackWish : DiffUtil.ItemCallback<WishListDetail>() {
    override fun areItemsTheSame(oldItem: WishListDetail, newItem: WishListDetail): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: WishListDetail, newItem: WishListDetail): Boolean {
        return oldItem == newItem
    }
}