package com.order.greenmart.ui.home.wishlist

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.order.greenmart.MainActivity
import com.order.greenmart.R
import com.order.greenmart.adapter.WishListAdapter
import com.order.greenmart.databinding.FragmentWishlistBinding


class WishlistFragment : Fragment() {


    private var _binding:FragmentWishlistBinding? = null
    private val binding get() = _binding

    private val viewModel: WishListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWishlistBinding.inflate(inflater,container,false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.viewModel = viewModel
        binding!!.lifecycleOwner = this
        binding!!.context = viewLifecycleOwner
        binding!!.recyclerview.adapter = WishListAdapter(viewLifecycleOwner,viewModel)



        viewModel.progressnotifier.observe(viewLifecycleOwner, Observer {
            binding!!.tvcart.text = ""
            when(it){
                1->{
                    binding!!.progressHorizontal.visibility = View.VISIBLE
                }
                2->{
                    binding!!.progressHorizontal.visibility = View.GONE
                    binding!!.tvcart.text = "Your WishList Is \n Empty!"
                }
                3->{
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
                        viewModel.reFrashWishList()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


    }




    override fun onResume() {
        super.onResume()
        viewModel.reFrashWishList()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}