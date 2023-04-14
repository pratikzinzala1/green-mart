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

    private lateinit var analytics: FirebaseAnalytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel.reFreshProductData()
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        analytics = Firebase.analytics



        binding.viewModel = viewModel
        viewlifecycleOwner = viewLifecycleOwner
        binding.lifecycleOwner = this
        binding.context = viewlifecycleOwner
        binding.recyclerview.adapter =
            ProductAdapter(requireContext(), viewLifecycleOwner, viewModel)


        var searchView: SearchView? = null

        activity?.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)


                val search = menu.findItem(R.id.action_search)
                searchView = search.actionView as SearchView

                searchView!!.setMaxWidth(Integer.MAX_VALUE);


                val searchEditText =
                    searchView!!.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
                searchEditText.setTextColor(requireActivity().getColor(R.color.Basilbackground))
                searchEditText.setHintTextColor(requireActivity().getColor(R.color.Basilbackground))


                searchView!!.queryHint = "Search.."
                searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }


                    override fun onQueryTextChange(newText: String?): Boolean {
                        throw RuntimeException("Test Crash")

                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_refersh -> {

                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)



        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {


                    if (!searchView!!.isIconified()) {
                        searchView!!.onActionViewCollapsed();
                    } else {
                        activity!!.finishAndRemoveTask()
                    }

                }
            }
            )




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


    override fun onDestroyView() {
        super.onDestroyView()

        println("ON Destroy Called")
        _binding = null
    }
}