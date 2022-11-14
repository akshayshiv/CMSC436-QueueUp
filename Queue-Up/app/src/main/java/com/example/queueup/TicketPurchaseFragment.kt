package com.example.queueup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.example.queueup.databinding.FragmentTicketPurchaseBinding


class TicketPurchaseFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentTicketPurchaseBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTicketPurchaseBinding.inflate(inflater, container, false)

        Log.i("", "Purchase-tickets")

        binding.add.setOnClickListener{
            checkout()
        }
        return binding.root
    }
    private fun checkout(){
        

    }


}