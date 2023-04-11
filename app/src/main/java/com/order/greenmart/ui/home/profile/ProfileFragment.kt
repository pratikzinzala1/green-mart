package com.order.greenmart.ui.home.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.order.greenmart.GreenMartApplication
import com.order.greenmart.MainActivity
import com.order.greenmart.R
import com.order.greenmart.databinding.FragmentHomeBinding
import com.order.greenmart.databinding.FragmentProfileBinding
import com.order.greenmart.ui.home.authentication.ForgotPasswordActivity


class ProfileFragment : Fragment() {


    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        val email:String
        val name:String
        if(GreenMartApplication.sharedPreferences!!.contains("EMAIL")){
            email = GreenMartApplication.sharedPreferences!!.getString("EMAIL",null).toString()
            if (!email.isNullOrEmpty()){
                binding.profileEmail.text = email
            }
        }
        if(GreenMartApplication.sharedPreferences!!.contains("NAME")){
            name = GreenMartApplication.sharedPreferences!!.getString("NAME",null).toString()
            if (!name.isNullOrEmpty()){
                binding.profilename.text = name
            }
        }
        binding.btnChangepassword.setOnClickListener {
            startActivity(Intent(activity,ForgotPasswordActivity::class.java))

        }



        return _binding!!.root
    }


}