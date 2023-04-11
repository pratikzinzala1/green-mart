package com.order.greenmart.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.order.greenmart.databinding.ItemOrderBinding
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.DataXXX


class OrderListAdapter :
    ListAdapter<DataXXX, OrderListAdapter.ItemViewholder>(DiffCallbackOrder()) {


    inner class ItemViewholder(private var binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: DataXXX) {

            binding.orderModel = item


            val date = java.lang.StringBuilder(item.createdAt)
            val newdate = date.split("T","Z",".")
            binding.orderDate.text  = "Ordered On : " + newdate[0] +" At  "+ newdate[1]
        }


    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderListAdapter.ItemViewholder {
        return ItemViewholder(ItemOrderBinding.inflate(LayoutInflater.from(parent.context)))

    }

    override fun onBindViewHolder(holder: OrderListAdapter.ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }


}

class DiffCallbackOrder : DiffUtil.ItemCallback<DataXXX>() {
    override fun areItemsTheSame(oldItem: DataXXX, newItem: DataXXX): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: DataXXX, newItem: DataXXX): Boolean {
        return oldItem == newItem
    }
}