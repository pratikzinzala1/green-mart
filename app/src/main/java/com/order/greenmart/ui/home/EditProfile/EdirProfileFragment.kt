package com.order.greenmart.ui.home.EditProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.order.greenmart.R

/**
 * A simple [Fragment] subclass.
 * Use the [EdirProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EdirProfileFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edir_profile, container, false)




        return view
    }


}