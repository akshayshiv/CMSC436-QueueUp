package com.example.queueup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.queueup.databinding.FragmentDashboardBinding
import com.example.queueup.databinding.FragmentTicketPurchaseBinding

class Dashboard: Fragment()  {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var purchaseFragment: FragmentTicketPurchaseBinding



    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        purchaseFragment = FragmentTicketPurchaseBinding.inflate(inflater, container, false)

        //setContentView(binding.root)


        Log.i("dash", "Dashboard-onCreate")

        //setSupportActionBar(binding.toolbar)

        binding.concert1.setOnClickListener {

            findNavController().navigate(
                R.id.action_DashboardFragment_to_concert1
            )

        }
        binding.concert2.setOnClickListener {

            findNavController().navigate(
                R.id.action_DashboardFragment_to_concert2
            )
        }
        binding.concert3.setOnClickListener {
            findNavController().navigate(
                R.id.action_DashboardFragment_to_concert3
            )
        }


        return binding.root

    }
}