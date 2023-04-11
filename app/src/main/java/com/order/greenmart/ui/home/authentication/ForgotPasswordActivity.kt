package com.order.greenmart.ui.home.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.order.greenmart.GreenMartApplication
import com.order.greenmart.MainActivity
import com.order.greenmart.retrofitdatabase.GreenMartApi
import com.order.greenmart.retrofitdatabase.requestmodel.PasswordRequest
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.UserResponse
import com.order.greenmart.databinding.ActivityForgotPasswordBinding
import com.order.greenmart.removeErrorAfterTextChange
import retrofit2.Call
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.inputlayoutnewpassword.removeErrorAfterTextChange()
        binding.inputlayoutconfirmpassword.removeErrorAfterTextChange()



        binding.btnReset.setOnClickListener {

            val newpassword = binding.inputnewpassword.text.toString()
            val confirm = binding.inputconfirmpassword.text.toString()

            println(newpassword)
            println(confirm)

            if (newpassword.isEmpty()) {
                binding.inputlayoutnewpassword.error = "Password Required"

            } else if (confirm.isEmpty()) {
                binding.inputlayoutconfirmpassword.error = "Confirm Password Required"
            } else if (newpassword != confirm) {
                binding.inputlayoutconfirmpassword.error = "Password Mismatch"
            } else {

                if(GreenMartApplication.sharedPreferences!!.contains("JWTTOKEN")){

                    resetPassword(
                        binding.inputnewpassword.text.toString(),
                        binding.inputconfirmpassword.text.toString(),
                        GreenMartApplication.sharedPreferences!!.getString("JWTTOKEN",null)!!
                    )

                }

            }
        }
    }


    fun resetPassword(newpassword: String, confirmpassword: String, jwtToken: String) {

        val passwordObj = PasswordRequest(newpassword, confirmpassword)
        val requestCall =
            GreenMartApi.retrofitService.changPassword(passwordObj, "Bearer $jwtToken")
        binding.progressHorizontal.visibility = View.VISIBLE

        requestCall.enqueue(object : retrofit2.Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {

                if (response.code() == 200) {
                    GreenMartApplication.editor!!.clear().commit()
                    startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
                    finishAffinity()
                    binding.progressHorizontal.visibility = View.INVISIBLE

                } else if (response.code() == 400) {
                    binding.progressHorizontal.visibility = View.INVISIBLE

                    Toast.makeText(this@ForgotPasswordActivity, "Error 400", Toast.LENGTH_SHORT)
                        .show()
                }

            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                binding.progressHorizontal.visibility = View.INVISIBLE

                Toast.makeText(
                    this@ForgotPasswordActivity,
                    "Something went wrong!!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })


    }
}