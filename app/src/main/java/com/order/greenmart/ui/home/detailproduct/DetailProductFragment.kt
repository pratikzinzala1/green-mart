package com.order.greenmart.ui.home.detailproduct

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.order.greenmart.AddToCart
import com.order.greenmart.MainActivity
import com.order.greenmart.R
import com.order.greenmart.databinding.FragmentDetailProductBinding
import com.order.greenmart.databinding.FragmentHomeBinding
import com.order.greenmart.retrofitdatabase.requestmodel.AddToCartRequest
import com.order.greenmart.ui.home.HomeViewModel


class DetailProductFragment : Fragment() {

    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentDetailProductBinding.inflate(inflater, container, false)


        return _binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = requireArguments().getInt("position")

        val product = viewModel.getsingleitem(position)

        binding.productDetail = product

        Glide.with(this).load(product.imageUrl).into(binding.productImage)




        if (product.cartItemId != null) {
            binding.togglegrp1.check(R.id.add_to_cart)
            binding.addToCart.text = "Go To Cart"
        }



        binding.togglegrp1.addOnButtonCheckedListener { group, checkedId, isChecked ->

            if (isChecked) {
                if (checkedId == R.id.add_to_cart) {
                    if (product.cartItemId == null) {
                        val obj = AddToCartRequest(product._id)
                        AddToCart(obj)
                        binding.addToCart.text = "Go to Cart"

                    }
                }


            } else {
                if (checkedId == R.id.add_to_cart) {
//

                    findNavController().navigate(R.id.nav_cart)

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


}