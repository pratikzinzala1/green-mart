package com.order.greenmart.ui.home.cart

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.order.greenmart.MainActivity
import com.order.greenmart.R
import com.order.greenmart.adapter.CartAdapter
import com.order.greenmart.checkForInternet
import com.order.greenmart.databinding.FragmentCartBinding
import com.order.greenmart.ui.home.HomeViewModel


class cartFragment : Fragment() {


    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding

    var CHANNEL_ID = "99"

    private val viewModel: HomeViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCartBinding.inflate(inflater, container, false)

        return _binding!!.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.viewModel = viewModel
        binding!!.lifecycleOwner = this
        binding!!.context = viewLifecycleOwner
        binding!!.recyclerview.adapter = CartAdapter(viewLifecycleOwner, viewModel)

        showNotification()

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
            binding!!.cartTotal.text = "$" + String.format("%.2f", it.toDouble())
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
                menuInflater.inflate(R.menu.main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
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

    @SuppressLint("MissingPermission")
    private fun showNotification(){
        createNotificationChannel()
        val notificationIntent = Intent(requireActivity(),MainActivity::class.java)
        notificationIntent.putExtra("CARTLAUNCH",true)

        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(requireActivity(), CHANNEL_ID)
            .setSmallIcon(R.drawable.japan_symbol)
            .setContentTitle("Your order has been placed")
            .setContentText("Order complete! Thank you so much for choosing us! Here are the details...")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(requireContext())) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }
    }


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = descriptionText
            // Register the channel with the system
            val notificationManager: NotificationManager =
                (requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            notificationManager.createNotificationChannel(channel)
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.reFreshCart()

    }


}