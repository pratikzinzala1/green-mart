package com.order.greenmart.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.order.greenmart.*
import com.order.greenmart.databinding.ItemProductBinding
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.ProductDetails
import com.order.greenmart.ui.home.HomeFragmentDirections
import com.order.greenmart.ui.home.HomeViewModel
import com.order.greenmart.ui.home.detailproduct.DetailProductFragment


class ProductAdapter(var context: Context, var life: LifecycleOwner, val viewModel: HomeViewModel) :
    ListAdapter<ProductDetails, ProductAdapter.ProductViewHolder>(DiffCallback3()) {


    inner class ProductViewHolder(
        private var binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(productModel: ProductDetails) {


            binding.productresponse = productModel
            binding.lifecycle = life
            binding.context = context




            binding.root.setOnClickListener {

                (context as MainActivity).navController.navigate(
                    HomeFragmentDirections.actionNavHomeToDetailProductFragment(
                        adapterPosition
                    )
                )

            }



            binding.addToCart.apply {
                if (productModel.cartItemId == null) {
                    this.text = "Add To Cart"
                    this.setTextColor(context.getColor(R.color.basilgreen800))
                    this.background =
                        context.resources.getDrawable(R.drawable.leftroundborder, context.theme)
                } else {
                    this.text = "Remove From Cart"
                    this.setTextColor(context.getColor(R.color.white))
                    this.background =
                        context.resources.getDrawable(R.drawable.leftroundfilled, context.theme)
                }

            }
            binding.addToFavourite.apply {
                if (productModel.watchListItemId == null) {
                    this.text = "Add To Wishlist"
                    this.setTextColor(context.getColor(R.color.basilgreen800))
                    this.background =
                        context.resources.getDrawable(R.drawable.rightroundborder, context.theme)
                } else {
                    this.text = "Remove From Wishlist"
                    this.setTextColor(context.getColor(R.color.white))
                    this.background =
                        context.resources.getDrawable(R.drawable.rightroundfilled, context.theme)

                }
            }

            binding.addToCart.setOnClickListener {

                productModel.addTocart(life, binding.addToCart, context, viewModel)

            }


            binding.addToFavourite.setOnClickListener {

                productModel.addToWishList(life, binding.addToFavourite, context, viewModel)

            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class DiffCallback3 : DiffUtil.ItemCallback<ProductDetails>() {
    override fun areItemsTheSame(oldItem: ProductDetails, newItem: ProductDetails): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: ProductDetails, newItem: ProductDetails): Boolean {
        return oldItem == newItem
    }
}