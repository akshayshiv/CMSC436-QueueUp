package com.example.queueup

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.example.queueup.databinding.FragmentTicketPurchaseBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


class TicketPurchaseFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentTicketPurchaseBinding
    private var inflight = false
    private var returned = false
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val args: TicketPurchaseFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        returned = false
        //dialogView =  inflater.inflate(R.layout., null)
        // Inflate the layout for this fragment
        binding = FragmentTicketPurchaseBinding.inflate(inflater, container, false)
        super.onCreate(savedInstanceState)

        Log.i("", "Purchase-tickets")
        try{
            binding.add.setOnClickListener {
                approvePurchase()

            }
            binding.tableRow1.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> checkout() }
        } catch (e: IllegalStateException){
            return binding.root
        }
        returned = true
        return binding.root



    }



    private fun sanitize_email (email: String?): String {
        return email?.replace(".",",") ?: ""

    }

    /* I give up dealing with the null stuff in kotlin lmao you can try fixing it if you want *sparkling* prettier *sparkling* code */
//    private fun get_user(user): Array<String> {
//
//        val email = user.email
//        val san_email = sanitize_email(email)
//
//        /* call sanitize_email(email) to the database to retrieve the user info... like the credit card */
//        Log.i("ticket", "name ${san_email}")
//        var Ref = firebaseDatabase.getReference()
//
//
//        var creditcard: String? = ""
//        Ref.child("users").child(san_email).get().addOnSuccessListener {
//            // Log.i("ticket", "value of it for creditcard ${it.child("creditcard").value}")
//
//            if (it.value != null) {
//                creditcard = it.child("creditcard").value as String?
//            }
//        }
//
//        return arrayOf(email, creditcard)
//
//    }

    private fun update_db_after_purchase() {
        val current_concert = args.myArg
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


            myRef.child(current_concert).child(san_email).get().addOnSuccessListener {

                Log.i("update_db", "value of it ${it}")

                if (it.value != null) {

                    Log.i("update_db", "user already bought before, increment")

                    var myRef = firebaseDatabase.getReference(current_concert)

                    myRef.child(san_email).setValue(it.value.toString().toInt() + 1)

                } else {
                    Log.i("update_db", "user have not bought before, add to db")

                    var myRef = firebaseDatabase.getReference(current_concert)

                    myRef.child(san_email).setValue(1)
                }

            }.addOnFailureListener {
                    Log.i("registration", "call to snapshot failed")
                }
            }


        }
    }



    private fun approvePurchase() {

        try{
            val btnShowAlert: Button = requireView().findViewById(R.id.add)
            btnShowAlert.setOnClickListener {
                // build alert dialog
                val dialogBuilder = AlertDialog.Builder(requireActivity())

                dialogBuilder.setMessage("Do you want to complete this purchase ?")
                    .setCancelable(true)
                    .setPositiveButton("Purchase", DialogInterface.OnClickListener { dialog, _ ->
                        activity?.onBackPressed()


                        Toast.makeText(
                            requireContext(),
                            "Purchase Successful!",
                            Toast.LENGTH_LONG
                        ).show()

                        update_db_after_purchase()
                        /* call checkout to update the current db */

                        dialog.dismiss()
                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                        findNavController().navigate(R.id.DashboardFragment)
                        dialog.cancel()
                    })

                val alert = dialogBuilder.create()
                alert.setTitle("Checkout?")
                alert.show()
            }
        }catch(e:IllegalStateException) {
            return
        }

    }

    private fun timer(init_time: Long) {
        val textView: TextView = requireView().findViewById(R.id.textView)

        try{
            val button: Button = requireView()?.findViewById(R.id.add)
            button.isEnabled = true
        } catch(e:IllegalStateException){

        }

        object : CountDownTimer(init_time, 1000) {

            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
                textView.setText("You have " + millisUntilFinished / 1000 + " seconds remaining to check out!")
            }

            // Callback function, fired
            // when the time is up
            override fun onFinish() {
                textView.setText("You ran out of time!")
                if(returned) {
                    try{
                        val button: Button = requireView()?.findViewById(R.id.add)
                        button.isEnabled = false
                    } catch(e:IllegalStateException){
                        return
                    }
                }
            }
        }.start()
    }

    private fun checkout(){

        Log.i("Ticket Purchase", args.myArg)

//        if(inflight){
//            Toast.makeText(
//                requireContext(),
//               "Already started checkout process!",
//                Toast.LENGTH_LONG
//            ).show()
//            return
//        }
//        inflight = true

        val textView: TextView = requireView().findViewById(R.id.textView)

        Log.i("", "check_out")
        val firebaseDatabase = FirebaseDatabase.getInstance()

        /* represents 30 second counter */
        val THRESHHOLD = 30


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



            /* Checks if the database already has a buyer */
            Ref.child("curr_buyer").child("buyer").get().addOnSuccessListener {

                Log.i("ticket", "value of it ${it}")

                var proceed = false
                if (it.value != null) {

                    /* buyer already exists, has their timer run out? */
                    val old_time = LocalDateTime.parse(it.child("currtime").value as CharSequence?)
                    val curr_time = LocalDateTime.now()

                    var time_diff = ChronoUnit.SECONDS.between(old_time, curr_time)

                    Log.i("ticket", "time difference of the entry and current time ${time_diff}")

                    /* OR, is the buyer ME? */
                    if (email == it.child("email").value) {

                        approvePurchase()

                        if (time_diff >= THRESHHOLD) {
                            proceed = true
                            time_diff = 30
                        }


                        timer(30000 - time_diff * 1000)


                        /* I AM THE BUYER, create purchase mechanism HERE */
                        Log.i("ticket", "I AM THE BUYER")
                    } else {

                        if (ChronoUnit.SECONDS.between(old_time, curr_time) >= THRESHHOLD) {
                            /* if the timer ran out, set proceed to true */
                            proceed = true

                        } else {
                            val textView1: TextView = requireView().findViewById(R.id.textView1)
                            textView.setText("Another buyer is in the queue right now!")

                            try{
                                val button: Button = requireView()?.findViewById(R.id.add)
                                button.isEnabled = false
                            } catch(e:IllegalStateException){

                            }

                            object : CountDownTimer(30000 - time_diff * 1000, 1000) {

                                // Callback function, fired on regular interval
                                override fun onTick(millisUntilFinished: Long) {

                                    textView1.setText("The buyer in queue has " + millisUntilFinished / 1000 + " seconds remaining to check out!")

                                }

                                // Callback function, fired
                                // when the time is up
                                override fun onFinish() {
                                    textView.setText("You can buy now!")
                                    textView1.setText("")


                                    /* Needed here to deal with multiple user trying to buy at once */
                                    val userRef = firebaseDatabase.getReference("curr_buyer")

                                    val userInfo = User_Info((LocalDateTime.now()).toString(), email, creditcard)

                                    userRef.child("buyer").setValue(userInfo)

                                    Log.i("ticket", "onFinish")
                                    proceed = true

                                    if(returned) {
                                        /* tries to handle cases when multiple people were waiting in the queue */
                                        checkout()
                                    }
                                }
                            }.start()

                        }

                    }


                }

                if (it.value == null || proceed){
                    Log.i("ticket", "safe to proceed, either no buyer exists or old_buyer timed out")
                    timer(30000)

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

