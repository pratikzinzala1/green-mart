package com.order.greenmart.ui.home.authentication


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.order.greenmart.retrofitdatabase.GreenMartApi
import com.order.greenmart.retrofitdatabase.requestmodel.OtpRequest
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.UserResponse
import com.order.greenmart.databinding.ActivityOtpverificationBinding
import `in`.aabhasjindal.otptextview.OTPListener
import retrofit2.Call
import retrofit2.Response


class Otpverification : AppCompatActivity() {


    private lateinit var binding: ActivityOtpverificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpverificationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val i = intent
        val UID = i.getStringExtra("UID")
        val sendyouemailtexg = i.getStringExtra("SENDYOUEMAIL")
        val forgotpassword = i.getBooleanExtra("FORGOTPASSWORD",false)
        binding.otptext.text = sendyouemailtexg


        binding.otpView.otpListener = object : OTPListener {

            override fun onInteractionListener() {
            }
            override fun onOTPComplete(otp: String) {
                if (forgotpassword){
                    verifyOtpForForgot(UID!!,otp)
                }
                else{
                    verifyOTP(otp, UID!!)
                }
            }
        }
        binding.tvresendotp.setOnClickListener {
            resendOtp(UID!!)
        }

    }

    private fun verifyOtpForForgot(UID:String,otp:String){
        val Otpobj = OtpRequest(otp, UID)
        val requestCall = GreenMartApi.retrofitService.verifyOtpOnForgot(Otpobj)

        requestCall.enqueue(object :
            retrofit2.Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {

                if (response.code() == 200) {

                    if (response.body()!!.status == 1) {
                        Toast.makeText(this@Otpverification,"We have Send You Password on Your Email", Toast.LENGTH_SHORT).show()
                        val i = Intent(this@Otpverification, LoginActivity::class.java)
                        startActivity(i)
                        finish()
                    }

                } else {
                    binding.otptext.text = "Invalid OTP"
                    binding.otpView.showError()

                }

            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {


            }

        }
        )

    }


    private fun verifyOTP(otp: String, UID: String) {
        val Otpobj = OtpRequest(otp, UID)
        val requestCall = GreenMartApi.retrofitService.verifyOtp(Otpobj)

        requestCall.enqueue(object :
            retrofit2.Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {

                if (response.code() == 200) {

                    if (response.body()!!.status == 1) {
                        startActivity(Intent(this@Otpverification, LoginActivity::class.java))
                        finish()
                    }

                } else {
                    binding.otptext.text = "Invalid OTP"
                    binding.otpView.showError()

                }

            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {


            }

        }
        )


    }


    private fun resendOtp(UID: String) {
        val Otpobj = OtpRequest(null, UID)
        val requestCall = GreenMartApi.retrofitService.resendOtp(Otpobj)

        requestCall.enqueue(object :
            retrofit2.Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            }

        }
        )


    }




}