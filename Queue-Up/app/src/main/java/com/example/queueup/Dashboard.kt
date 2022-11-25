package com.example.queueup

import android.content.Intent
import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.queueup.databinding.FragmentDashboardBinding
class Dashboard: Fragment()  {

    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
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