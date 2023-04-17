package com.order.greenmart.ui.home.cart

import android.Manifest
import android.animation.Animator
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.NavDeepLinkBuilder
import com.order.greenmart.GreenMartApplication
import com.order.greenmart.MainActivity
import com.order.greenmart.R
import com.order.greenmart.adapter.CartAdapter
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


        var total: String = "0.0"
        binding!!.lottieanimation.setAnimation("orderconfirmed.json")

        binding!!.btnplaceorder.setOnClickListener {


            if (total.toDouble() > 0.0) {

                val live = viewModel.placeOrder(total)

                live.observe(viewLifecycleOwner) {
                    if (it) {

                        showNotification()

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


    }


    private fun showNotification() {

        if (GreenMartApplication.sharedPreferences!!.getBoolean("CHECKED",false)){
            createNotificationChannel()


            val pendingIntent = NavDeepLinkBuilder(requireActivity().applicationContext)
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.mobile_navigation)
                .setDestination(R.id.nav_orderlist)
                .createPendingIntent()


            val builder = NotificationCompat.Builder(requireActivity().applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.japan_symbol)
                .setContentTitle("Your order has been placed")
                .setContentText("Order complete! Thank you so much for choosing us! Here are the details...")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)

            with(NotificationManagerCompat.from(requireActivity().applicationContext)) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf( Manifest.permission.POST_NOTIFICATIONS), 123 )
                    }

                    return
                }
                notify(1, builder.build())
            }

        }

    }


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewModel.finishProgress()
    }




}