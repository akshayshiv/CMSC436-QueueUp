package com.example.queueup

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.queueup.databinding.FragmentAccountinfoBinding
import com.example.queueup.databinding.NavDrawerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class AccountInfoActivity: AppCompatActivity() {

    private lateinit var binding: FragmentAccountinfoBinding

    private var arr: MutableList<String> = ArrayList<String>()

    private fun sanitize_email(email: String?): String {
        return email?.replace(".", ",") ?: ""

    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = FragmentAccountinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.i("Accountinfo", "Accountinfo oncreate")


    }

    override fun onStart() {
        super.onStart()
        val v: View = binding.root

        val firebaseDatabase = FirebaseDatabase.getInstance()
        var myRef = firebaseDatabase.getReference()
        val user = FirebaseAuth.getInstance().currentUser


        if (user != null) {
            // User is signed in

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

            //setSupportActionBar(binding.toolbar)
            Log.i("account info", "set email ${email}")
            val emailtextView: TextView = v.findViewById(com.example.queueup.R.id.email)
            emailtextView.setText(email)


            val concerts =  arrayOf("concert1", "concert2", "concert3")

            var purchased_tickets: MutableList<Int> = arrayListOf()

            var purchasedevents = ""

            for (current_concert in concerts) {

                myRef.child(current_concert).child(san_email).get().addOnSuccessListener {

                    Log.i("account info", "value of it ${it}")

                    if (it.value != null) {

                        Log.i("account info", "user already bought before, increment")

                        var myRef = firebaseDatabase.getReference(current_concert)

                        purchased_tickets.add(it.value.toString().toInt())
                        purchasedevents = purchasedevents + current_concert
                        purchasedevents = purchasedevents + ": " + it.value.toString() + "\n"
                        val concerttextView: TextView = v.findViewById(com.example.queueup.R.id.purchasedevents)
                        concerttextView.setText(purchasedevents)

                    } else {
                        purchasedevents = purchasedevents + current_concert
                        purchasedevents = purchasedevents + ": 0\n"
                        val concerttextView: TextView = v.findViewById(com.example.queueup.R.id.purchasedevents)
                        concerttextView.setText(purchasedevents)
                    }


                }.addOnFailureListener {
                    Log.i("account info", "call to snapshot failed")
                    purchasedevents = purchasedevents + current_concert
                    purchasedevents = purchasedevents + ": 0\n"
                    val concerttextView: TextView = v.findViewById(com.example.queueup.R.id.purchasedevents)
                    concerttextView.setText(purchasedevents)
                }
            }


//            for (i in concerts.indices ) {
//                purchasedevents = purchasedevents + concerts[i]
//                purchasedevents = purchasedevents + ": " + purchased_tickets[i].toString()
//            }
//
//            val concerttextView: TextView = v.findViewById(com.example.queueup.R.id.purchasedevents)
//            concerttextView.setText(purchasedevents)


        }
    }
    override fun onResume() {
        super.onResume()

        onStart()

    }
}

