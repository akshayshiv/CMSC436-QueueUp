package com.example.queueup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.queueup.databinding.FragmentTicketPurchaseBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit



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

    private fun sanitize_email (email: String?): String {
        return email?.replace(".",",") ?: ""

    }

    private fun checkout(){

        Log.i("", "check_out")
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val myRef = firebaseDatabase.getReference("current_buyer")

        /* represents 30 second counter */
        val THRESHHOLD = 70


        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            val name = user.displayName
            val email = user.email
            val san_email = sanitize_email(email)

            /* call sanitize_email(email) to the database to retrieve the user info... like the credit card */
            Log.i("ticket", "name ${san_email}")
            var Ref = firebaseDatabase.getReference()


            var creditcard: String? = ""
            Ref.child("users").child(san_email).get().addOnSuccessListener {
                // Log.i("ticket", "value of it for creditcard ${it.child("creditcard").value}")

                if (it.value != null) {
                    creditcard = it.child("creditcard").value as String?
                }

            }


            /* Checks if the database already has a buyer */
            Ref.child("curr_buyer").child("buyer").get().addOnSuccessListener {

                Log.i("ticket", "value of it ${it}")

                var proceed = false
                if (it.value != null) {

                    /* buyer already exists, has their timer run out? */

                    /* OR, is the buyer ME? */
                    if (email == it.child("email").value) {

                        /* I AM THE BUYER, create purchase mechanism HERE */

                        Log.i("ticket", "I AM THE BUYER")
                    }

                    val old_time = LocalDateTime.parse(it.child("currtime").value as CharSequence?)
                    val curr_time = LocalDateTime.now()

                    val time_diff = ChronoUnit.SECONDS.between(old_time, curr_time)
                    Log.i("ticket", "time difference of the entry and current time ${time_diff}")

                    if (ChronoUnit.SECONDS.between(old_time, curr_time) >= THRESHHOLD) {
                        proceed = true

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "${THRESHHOLD - time_diff} seconds left for you to be able to purchase tickets",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    /* if the timer ran out, set proceed to true */
                    proceed = true
                }

                if (it.value == null || proceed){
                    Log.i("ticket", "safe to proceed, either no buyer exists or old_buyer timed out")

                    val userRef = firebaseDatabase.getReference("curr_buyer")

                    val userInfo = User_Info((LocalDateTime.now()).toString(), email, creditcard)

                    userRef.child("buyer").setValue(userInfo)

                }


            }.addOnFailureListener{
                Log.i("ticket", "call to snapshot failed")
            }

        } else {
            // No user is signed in
        }






    }


}