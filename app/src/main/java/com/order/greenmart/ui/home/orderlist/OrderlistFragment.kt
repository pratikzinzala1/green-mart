package com.order.greenmart.ui.home.orderlist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.order.greenmart.R
import com.order.greenmart.adapter.OrderListAdapter
import com.order.greenmart.adapter.WishListAdapter
import com.order.greenmart.databinding.FragmentOrderlistBinding
import com.order.greenmart.ui.home.wishlist.WishListViewModel


class OrderlistFragment : Fragment() {



    private var _binding:FragmentOrderlistBinding? = null
    private val binding get() = _binding

    private val viewModel: OrderlistViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentOrderlistBinding.inflate(inflater,container,false)

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.viewModel = viewModel
        binding!!.lifecycleOwner = this
        binding!!.context = viewLifecycleOwner
        binding!!.recyclerview.adapter = OrderListAdapter()

        viewModel.reFreshOrderList()



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

                menuInflater.inflate(R.menu.main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_refersh -> {
                        viewModel.reFreshOrderList()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


    }



}