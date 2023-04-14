package com.order.greenmart.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.order.greenmart.databinding.ItemCartBinding
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.CartDetail
import com.order.greenmart.ui.home.HomeViewModel



class CartAdapter(val viewLifecycleOwner: LifecycleOwner, val viewModel: HomeViewModel) :
    ListAdapter<CartDetail, CartAdapter.ItemViewholder>(DiffCallback2()) {


    inner class ItemViewholder(private var binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: CartDetail) {
            binding.cartProductDetail = item


            binding.totalperitem.text = "$" + String.format("%.2f", item.itemTotal!!.toDouble())

            binding.itemIncrease.setOnClickListener {

                item.increaseItem(viewModel,binding.productItemCount,binding.totalperitem)

            }
            binding.itemDecrease.setOnClickListener {

                item.decreaseItem(viewModel,binding.productItemCount,binding.totalperitem)

            }



            binding.addToCart.setOnClickListener {

                item.removeFromCart(viewModel,this@CartAdapter,adapterPosition)

            }

        }

    }

    override fun onCurrentListChanged(
        previousList: MutableList<CartDetail>,
        currentList: MutableList<CartDetail>
    ) {
        super.onCurrentListChanged(previousList, currentList)

        if (previousList.size ==1 && currentList.isEmpty()){
            viewModel.privateRefreshCart()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(ItemCartBinding.inflate(LayoutInflater.from(parent.context)))

    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }


}

class DiffCallback2 : DiffUtil.ItemCallback<CartDetail>() {
    override fun areItemsTheSame(oldItem: CartDetail, newItem: CartDetail): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: CartDetail, newItem: CartDetail): Boolean {
        return oldItem == newItem
    }
}