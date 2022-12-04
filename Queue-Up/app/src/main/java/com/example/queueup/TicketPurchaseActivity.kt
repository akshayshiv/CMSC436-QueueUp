package com.example.queueup

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.queueup.databinding.FragmentTicketPurchaseBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


class TicketPurchaseActivity : AppCompatActivity()  {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentTicketPurchaseBinding
    private var inflight = false
    private var returned = false
    val firebaseDatabase = FirebaseDatabase.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        returned = false

        // Inflate the layout for this fragment
        binding = FragmentTicketPurchaseBinding.inflate(layoutInflater)

        setContentView(binding.root)

        Log.i("", "Purchase-tickets")

        binding.add.setOnClickListener {
            approvePurchase()

        }
        binding.tableRow1.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> checkout() }



    }



    private fun sanitize_email (email: String?): String {
        return email?.replace(".",",") ?: ""

    }



    private fun update_db_after_purchase() {


        var current_concert = intent.getStringExtra("myArg")

        if (current_concert == null) {
            current_concert = ""
        }

        Log.i("ticket", current_concert.toString())


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
            val btnShowAlert: Button = binding.root.findViewById(R.id.add)
            btnShowAlert.setOnClickListener {
                // build alert dialog
                val dialogBuilder = AlertDialog.Builder(this)

                dialogBuilder.setMessage("Do you want to complete this purchase ?")
                    .setCancelable(true)
                    .setPositiveButton("Purchase", DialogInterface.OnClickListener { dialog, _ ->
                        this.onBackPressed()


                        Toast.makeText(
                            this,
                            "Purchase Successful!",
                            Toast.LENGTH_LONG
                        ).show()

                        update_db_after_purchase()
                        /* call checkout to update the current db */

                        dialog.dismiss()
                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
//                        findNavController().navigate(R.id.DashboardFragment)
                        finish()
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
        val textView: TextView = binding.root.findViewById(R.id.textView)

        try{
            val button: Button = binding.root.findViewById(R.id.add)
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
                        val button: Button = binding.root.findViewById(R.id.add)
                        button.isEnabled = false
                    } catch(e:IllegalStateException){
                        return
                    }
                }
            }
        }.start()
    }

    private fun checkout(){

//        Log.i("Ticket Purchase", args.myArg)

//        if(inflight){
//            Toast.makeText(
//                requireContext(),
//               "Already started checkout process!",
//                Toast.LENGTH_LONG
//            ).show()
//            return
//        }
//        inflight = true

        val textView: TextView = binding.root.findViewById(R.id.textView)

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
                            val textView1: TextView = binding.root.findViewById(R.id.textView1)
                            textView.setText("Another buyer is in the queue right now!")

                            try{
                                val button: Button = binding.root.findViewById(R.id.add)
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

                                    val userInfo = UserInfoClass((LocalDateTime.now()).toString(), email, creditcard)

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

                    val userInfo = UserInfoClass((LocalDateTime.now()).toString(), email, creditcard)

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

