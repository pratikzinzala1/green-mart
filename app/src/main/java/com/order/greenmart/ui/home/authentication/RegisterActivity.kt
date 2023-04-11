package com.order.greenmart.ui.home.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.order.greenmart.*
import com.order.greenmart.retrofitdatabase.GreenMartApi
import com.order.greenmart.retrofitdatabase.requestmodel.RegisterUserRequest
import com.order.greenmart.retrofitdatabase.requestResponseDataModel.UserResponse
import com.order.greenmart.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.inputlayoutemail.removeErrorAfterTextChange()
        binding.inputlayoutpassword.removeErrorAfterTextChange()
        binding.inputlayoutname.removeErrorAfterTextChange()
        binding.inputlayoutnumber.removeErrorAfterTextChange()

        binding.btnRegister.setOnClickListener {

            val email = binding.inputemail.text.toString()
            val password = binding.inputpassword.text.toString()
            val name = binding.inputName.text.toString()
            val phone = binding.inputPhoneNumber.text.toString()

            if (email.isEmpty()) {
                binding.inputlayoutemail.error = "Enter Email First"
            } else if (!email.isValidEmail()) {
                binding.inputlayoutemail.error = "Enter Valid Email First"
            } else if (password.isEmpty()) {
                binding.inputlayoutpassword.error = "Enter password First"
            } else if (name.isEmpty()) {
                binding.inputlayoutname.error = "Enter name First"
            } else if (!name.isValidName()) {
                binding.inputlayoutname.error = "Enter Valid Name"
            } else if (phone.isEmpty()) {
                binding.inputlayoutnumber.error = "Enter Phone Number First"
            } else if (!phone.isValidPhone()) {
                binding.inputlayoutnumber.error = "Enter Valid Phone Number"
            } else {
                registerUser(
                    email, password, name, phone
                )
            }


        }


    }


    private fun registerUser(email: String, password: String, name: String, phone: String) {
        binding.progressHorizontal.visibility = View.VISIBLE


        val registeruserobj = RegisterUserRequest(email, phone, name, password)
        val requestCall = GreenMartApi.retrofitService.registeruser(registeruserobj)

        requestCall.enqueue(object :
            retrofit2.Callback<UserResponse> {

            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {

                if (response.code() == 201) {

                    Toast.makeText(
                        this@RegisterActivity,
                        "Successful",
                        Toast.LENGTH_LONG
                    ).show()

                    if (response.body()!!.status == 1) {
                        binding.progressHorizontal.visibility = View.INVISIBLE

                        val i = Intent(this@RegisterActivity, Otpverification::class.java)
                        i.putExtra("UID", response.body()!!.data!!._id)
                        i.putExtra("SENDYOUEMAIL", response.body()!!.msg)
                        startActivity(i)
                        finish()

                    }

                } else if (response.code() == 400) {
                    binding.inputlayoutemail.error = "User Already Exist"
                    binding.progressHorizontal.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {

                this@RegisterActivity.somethingwentwrong()
                binding.progressHorizontal.visibility = View.INVISIBLE

            }

        })

    }


}



