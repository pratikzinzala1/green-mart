package com.order.greenmart.ui.home.cart

import android.animation.Animator
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.order.greenmart.MainActivity
import com.order.greenmart.R
import com.order.greenmart.adapter.CartAdapter
import com.order.greenmart.checkForInternet
import com.order.greenmart.databinding.FragmentCartBinding


class cartFragment : Fragment() {


    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding

    private val viewModel: CartViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCartBinding.inflate(inflater, container, false)

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.viewModel = viewModel
        binding!!.lifecycleOwner = this
        binding!!.context = viewLifecycleOwner
        binding!!.recyclerview.adapter = CartAdapter(viewLifecycleOwner, viewModel)


        var total: String = "0.0"
        binding!!.lottieanimation.setAnimation("orderconfirmed.json")

        binding!!.btnplaceorder.setOnClickListener {

            if (checkForInternet(requireContext())) {

                if (total.toDouble() > 0.0) {

                    val live = viewModel.placeOrder(total)

                    live.observe(viewLifecycleOwner) {
                        if (it) {
                            binding!!.lottieanimation.visibility = View.VISIBLE
                            binding!!.lottieanimation.playAnimation()
                            binding!!.lottieanimation.addAnimatorListener(object :
                                Animator.AnimatorListener {
                                override fun onAnimationStart(p0: Animator) {
                                }

                                override fun onAnimationEnd(p0: Animator) {
                                    binding!!.lottieanimation.visibility = View.GONE
                                }

                                override fun onAnimationCancel(p0: Animator) {
                                }

                                override fun onAnimationRepeat(p0: Animator) {
                                }

                            })
                        }

                    }
                }else{
                    (activity as MainActivity).showSnackBar("Add Product First")

                }


            } else {
                (activity as MainActivity).showSnackBar("No Internet")
            }

        }


        viewModel.cartTotalPrice.observe(viewLifecycleOwner, Observer {
            total = String.format("%.0f", it.toDouble())
            println(total)
            binding!!.cartTotal.text = "$" + String.format("%.1f", it.toDouble())
        })

        viewModel.progressnotifier.observe(viewLifecycleOwner, Observer {
            binding!!.tvcart.text = ""
            when (it) {
                1 -> {
                    binding!!.progressHorizontal.visibility = View.VISIBLE
                }
                2 -> {

                    binding!!.progressHorizontal.visibility = View.GONE
                    binding!!.tvcart.text = "Your Cart Is \n Empty!"
                }
                3 -> {
                    binding!!.progressHorizontal.visibility = View.GONE

                }
            }

        })

        activity?.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_refersh -> {
                        viewModel.reFreshCart()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


    }

    override fun onResume() {
        super.onResume()
        viewModel.reFreshCart()

    }


}