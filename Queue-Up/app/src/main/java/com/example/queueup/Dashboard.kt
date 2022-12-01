package com.example.queueup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.queueup.databinding.FragmentDashboardBinding
import com.example.queueup.databinding.FragmentTicketPurchaseBinding
import com.google.protobuf.LazyStringArrayList
import java.io.Serializable

class Dashboard: Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private var arr: MutableList<String> = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(savedInstanceState != null){
            arr = savedInstanceState.getSerializable("list") as MutableList<String>
        } else {
            if(arr != null) {
                //do nothing
            } else {
                arr = ArrayList()
            }
        }
        super.onCreate(savedInstanceState)
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        Log.i("dash", "Dashboard-onCreate")

        //setSupportActionBar(binding.toolbar)

        binding.concert1.setOnClickListener {

            findNavController().navigate(
                R.id.action_DashboardFragment_to_concert1,
            )

            arr.add("Hip Hop Concert")


        }
        binding.concert2.setOnClickListener {

            findNavController().navigate(
                R.id.action_DashboardFragment_to_concert2
            )
            arr.add("Rock Concert")


        }
        binding.concert3.setOnClickListener {
            findNavController().navigate(
                R.id.action_DashboardFragment_to_concert3
            )
            arr.add("Country Concert")

        }
        binding.textView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> update() }



        //binding.textView.add




        return binding.root

    }

    private fun update(){
        Log.i("dash", "Dashboard-update")
        if(arr.size == 0){
            return
        }
        val text: TextView = requireView()?.findViewById(R.id.MyEvents)
        text.text = "My Upcoming Event: \n " + arr[arr.size - 1]


    }
}

