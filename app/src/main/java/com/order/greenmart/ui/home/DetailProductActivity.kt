package com.order.greenmart.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.order.greenmart.AddToCart
import com.order.greenmart.MainActivity
import com.order.greenmart.R
import com.order.greenmart.RemoveFromCart
import com.order.greenmart.databinding.ActivityDetailProductBinding
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.ProductDetails
import com.order.greenmart.retrofitdatabase.requestmodel.AddToCartRequest
import com.order.greenmart.retrofitdatabase.requestmodel.CartItemRequest


class DetailProductActivity : AppCompatActivity() {


    lateinit var binding: ActivityDetailProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)






        val i = intent.extras



        var product =
            ProductDetails(
                null,
                i!!.getString("_ID")!!,
                null,
                i.getString("DESCRIPTION")!!,
                i.getString("IMAGEURL")!!,
                i.getString("PRICE")!!,
                i.getString("TITLE")!!,
                null,
                i.getString("CARTITEMID", null),
                i.getInt("QUANTITY", 0),
                i.getString("WATCHLISTITEMID", null)
            )



        binding.productDetail = product

        Glide.with(this).load(product.imageUrl).into(binding.productImage)


        binding.imgback.setOnClickListener {
            finish()
        }

        if (product.cartItemId != null){
            binding.togglegrp1.check(R.id.add_to_cart)
            binding.addToCart.text = "Go To Cart"
        }



        binding.togglegrp1.addOnButtonCheckedListener { group, checkedId, isChecked ->

            if (isChecked) {
                if (checkedId == R.id.add_to_cart) {
                    if (product.cartItemId == null){
                        val obj = AddToCartRequest(product._id)
                       AddToCart(obj)

                        binding.addToCart.text = "Go to Cart"

                    }
                }


            }
            else{
                if (checkedId == R.id.add_to_cart){
                    val intent = Intent(this,MainActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY)
                    intent.putExtra("FROMDETAIL",true)

                    startActivity(intent)
                    finishAndRemoveTask()
                }

//                if (checkedId == R.id.add_to_cart) {
//
//                    if (product.quantity!!>0){
//                        binding.togglegrp1.check(R.id.add_to_cart)
//
//                        val obj = CartItemRequest(product.cartItemId!!)
//                        val live = RemoveFromCart(obj)
//
//                        live.observe(this){
//                            product.cartItemId = null
//                            product.quantity = 0
//                        }
//
//                        binding.addToCart.text = "Add To Cart"
//
//                    }
//
//
//                }

            }


        }


    }

//    private fun addtocart(product:ProductDetails){
//        if (product.cartItemId != null) {
//
//
//            val obj = CartItemRequest(product.cartItemId!!)
//            val live = RemoveFromCart(obj)
//
//            live.observe(this, Observer {
//
//                //viewModel.privateReFreshProductData()
//                //isInCartList = false
//
//            })
////            cartItemId = null
////            quantity = null
////            view.text = "Add To Cart"
////
////            view.setTextColor(context.getColor(R.color.basilgreen800))
////            view.background = context.resources.getDrawable(R.drawable.leftroundborder,context.theme)
//
//
//        } else  {
//
//          //  isInCartList = true
//            val obj = AddToCartRequest(product._id)
//            val live = AddToCart(obj)
//            live.observe(life, Observer {
//
//                isInCartList = false
//
//                cartItemId = it.productDetails._id
//                quantity = it.quantity
//                //  viewModel.privateReFreshProductData()
//            })
//            cartItemId = "0"
//            quantity = 0
//            view.text = "Remove From Cart"
//
//            view.setTextColor(context.getColor(R.color.white))
//            view.background = context.resources.getDrawable(R.drawable.leftroundfilled,context.theme)
//
//
//        }
//    }




}