package com.order.greenmart.ui.home


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.order.greenmart.GreenMartApplication
import com.order.greenmart.MainActivity
import com.order.greenmart.R
import com.order.greenmart.ui.home.authentication.LoginActivity

class SplashScreenActivity : AppCompatActivity() {

    lateinit var i: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        i = Intent(this, LoginActivity::class.java)
        if(GreenMartApplication.sharedPreferences!!.contains("JWTTOKEN")){
            if (!GreenMartApplication.sharedPreferences!!.getString("JWTTOKEN",null).isNullOrEmpty()){
                i = Intent(this, MainActivity::class.java)
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(i)
            finish()
        }, 1000)




    }

}