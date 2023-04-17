package com.order.greenmart.ui.home

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.order.greenmart.MainActivity
import com.order.greenmart.R
import com.order.greenmart.adapter.ProductAdapter
import com.order.greenmart.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()

    private var viewlifecycleOwner: LifecycleOwner? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel.reFreshProductData()

       // val a = 1/0
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.viewModel = viewModel
        viewlifecycleOwner = viewLifecycleOwner
        binding.lifecycleOwner = this
        binding.context = viewlifecycleOwner
        binding.recyclerview.adapter =
            ProductAdapter(requireContext(), viewLifecycleOwner, viewModel)


        viewModel.reFreshProductData()





        viewModel.progressnotifier.observe(viewLifecycleOwner) {

            when (it) {
                1 -> {
                    binding.progressHorizontal.visibility = View.VISIBLE
                    binding.recyclerview.visibility = View.GONE
                }
                2 -> {
                    binding.progressHorizontal.visibility = View.GONE
                    binding.recyclerview.visibility = View.VISIBLE

                }
                3 -> {
                    binding.progressHorizontal.visibility = View.GONE
                    binding.recyclerview.visibility = View.VISIBLE

                }
            }

        }

        viewModel.logout.observe(viewLifecycleOwner) {

            if (it == true) {
                (activity as MainActivity).logoutuser()
            }

        }


        viewModel.totalcartquantity.observe(viewlifecycleOwner!!) {
            binding.fab.text = "Quantity : " + it.toString()
        }


        binding.fab.setOnClickListener {


            findNavController().navigate(R.id.nav_cart)

        }

        binding.recyclerview.setOnScrollChangeListener(object : View.OnScrollChangeListener {
            override fun onScrollChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int) {

                if (p2 > p4 + 12 && binding.fab.isExtended) {
                    binding.fab.shrink()
                }

                if (p2 < p4 - 12 && !binding.fab.isExtended) {
                    binding.fab.extend()
                }

                if (p2 == 0) {
                    binding.fab.extend()
                }

            }

        })


    }


    override fun onResume() {
        super.onResume()

        viewModel.privateReFreshProductData()
        viewModel.apiCallCount = 0
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewModel.finishProgress()
    }



}