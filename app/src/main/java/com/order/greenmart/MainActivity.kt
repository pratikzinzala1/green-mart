package com.order.greenmart

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.ui.*
import com.google.android.material.snackbar.Snackbar
import com.order.greenmart.databinding.ActivityMainBinding
import com.order.greenmart.ui.home.HomeFragment
import com.order.greenmart.ui.home.HomeFragmentDirections
import com.order.greenmart.ui.home.authentication.LoginActivity
import com.order.greenmart.ui.home.cart.cartFragment
import com.order.greenmart.ui.home.orderlist.OrderlistFragment
import com.order.greenmart.ui.home.profile.ProfileFragment
import com.order.greenmart.ui.home.wishlist.WishlistFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, arrayOf( Manifest.permission.POST_NOTIFICATIONS), 123 )
        }


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)


        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_wishlist,
                R.id.nav_wishlist,
                R.id.nav_cart,
                R.id.nav_profile,
                R.id.nav_orderlist
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        navView.menu.findItem(R.id.nav_home).setOnMenuItemClickListener {
            navController.navigate(R.id.nav_home)
            true
        }

        navView.menu.findItem(R.id.logout).setOnMenuItemClickListener {
            logoutuser()
            true
        }



    }

    override fun onResume() {
        super.onResume()

    }




    fun logoutuser() {
        GreenMartApplication.editor!!.clear().commit()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }


    fun showSnackBar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}