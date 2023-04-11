package com.order.greenmart

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
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

//            if (GreenMartApplication.sharedPreferences!!.contains("JWTTOKEN")) {
//                GreenMartApplication.editor!!.clear().commit()
//                finishAffinity()
//            }
            true
        }

    }

    fun logoutuser(){
        GreenMartApplication.editor!!.clear().commit()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)

    }


    fun showSnackBar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)



        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}