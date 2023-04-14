package com.order.greenmart.ui.home.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.order.greenmart.*
import com.order.greenmart.databinding.ActivityLoginBinding
import com.order.greenmart.retrofitdatabase.GreenMartApi
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.UserResponse
import com.order.greenmart.retrofitdatabase.requestmodel.RegisterUserRequest
import retrofit2.Call
import retrofit2.Response


class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.inputlayoutemail.removeErrorAfterTextChange()
        binding.inputlayoutpassword.removeErrorAfterTextChange()

        binding.btnlogin.setOnClickListener {


            val email = binding.inputemail.text!!.toString()
            val password = binding.inputpassword.text!!.toString()

            if (email.isEmpty()) {
                binding.inputlayoutemail.error = "Enter Your Email First"
            } else if (!email.isValidEmail()) {
                binding.inputlayoutemail.error = "Enter Valid Email"
            } else if (password.isEmpty()) {
                binding.inputlayoutpassword.error = "Enter Your Password First"
            } else {
                userLogin(binding.inputemail.text.toString(), binding.inputpassword.text.toString())
            }


        }
        binding.tvNewuser.setOnClickListener {

            startActivity(Intent(this, RegisterActivity::class.java))

        }
        binding.ForgotPassword.setOnClickListener {
            val email = binding.inputemail.text!!.toString()

            binding.inputlayoutemail.error = null
            binding.inputlayoutpassword.error = null

            if (binding.inputemail.text!!.isEmpty()) {
                binding.inputlayoutemail.error = "Enter Email First"
            } else if (email.isValidEmail()) {
                forgotEmail(binding.inputemail.text.toString())
            } else {
                binding.inputlayoutemail.error = "Enter Valid Email"

            }

        }

    }

    private fun forgotEmail(email: String) {
        binding.inputemail.error = null
        binding.inputlayoutpassword.error = null
        val registeruserobj = RegisterUserRequest(email, null, null, null)
        val requestCall = GreenMartApi.retrofitService.forgotPassword(registeruserobj)
        binding.progressHorizontal.visibility = View.VISIBLE

        requestCall.enqueue(object : retrofit2.Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>, response: Response<UserResponse>
            ) {

                if (response.code() == 200) {
                    binding.progressHorizontal.visibility = View.INVISIBLE

                    val i = Intent(this@LoginActivity, Otpverification::class.java)
                    i.apply {
                        putExtra("UID", response.body()!!.data!!._id)
                        putExtra("SENDYOUEMAIL", response.body()!!.msg)
                        putExtra("FORGOTPASSWORD", true)
                    }
                    startActivity(i)
                    finish()

                } else if (response.code() == 400) {
                    binding.progressHorizontal.visibility = View.INVISIBLE

                    binding.inputlayoutemail.error = "User Not Registered!"

                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                this@LoginActivity.somethingwentwrong()
                binding.progressHorizontal.visibility = View.INVISIBLE
            }

        })


    }


    private fun userLogin(email: String, password: String) {

        val obj = RegisterUserRequest(email, null, null, password)
        val requestCall = GreenMartApi.retrofitService.loginUser(obj)

        binding.progressHorizontal.visibility = View.VISIBLE

        requestCall.enqueue(object : retrofit2.Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>, response: Response<UserResponse>
            ) {

                if (response.code() == 200) {

                    binding.progressHorizontal.visibility = View.INVISIBLE
                    Log.d("JWT",response.body()!!.toString())

                    GreenMartApplication.editor.apply {
                        this!!.putString("PHONENO", response.body()!!.data!!.mobileNo)
                        putString("EMAIL", response.body()!!.data!!.emailId)
                        putString("UID", response.body()!!.data!!._id)
                        putString("JWTTOKEN", response.body()!!.data!!.jwtToken)
                        putString("NAME", response.body()!!.data!!.name)
                    }!!.commit()


                    Log.d("GETJWT","LOGIN ${response.body()!!.data!!.jwtToken}")



                    val i = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(i)
                    finish()

                }
                else if (response.code() == 400) {
                    binding.inputlayoutemail.error = "Invalid username or password"
                    binding.inputlayoutpassword.error = "Invalid username or password"
                    binding.progressHorizontal.visibility = View.INVISIBLE
                }

            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                this@LoginActivity.somethingwentwrong()
                binding.progressHorizontal.visibility = View.INVISIBLE
            }
        })

    }



}




