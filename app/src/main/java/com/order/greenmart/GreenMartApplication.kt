package com.order.greenmart

import android.app.Application
import android.content.*
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Binder
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.order.greenmart.ui.home.authentication.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GreenMartApplication:Application() {




    companion object{
        var sharedPreferences:SharedPreferences? = null
        var editor:SharedPreferences.Editor? = null

    }

    lateinit var networkChangeReceiver: BroadcastReceiver


    override fun onCreate() {
        super.onCreate()

        networkChangeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (!isNetworkAvailable(this@GreenMartApplication)) {
                    val i = Intent(this@GreenMartApplication, ShowMessage::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(i)
                    return
                }

            }

        }

        registerReceiver(
            networkChangeReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )



        sharedPreferences = getSharedPreferences("GREENMART", MODE_PRIVATE)
        editor = sharedPreferences!!.edit()

    }


    fun isNetworkAvailable(context: Context) =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
            getNetworkCapabilities(activeNetwork)?.run {
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            } ?: false
        }





}






