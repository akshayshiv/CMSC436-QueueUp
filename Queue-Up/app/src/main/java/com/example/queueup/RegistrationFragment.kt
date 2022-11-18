package com.example.queueup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.queueup.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime


class RegistrationFragment : Fragment() {

    private var validator = Validators()
    private lateinit var auth: FirebaseAuth

    private lateinit var binding: FragmentRegisterBinding


    /** Binding to XML layout */


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Use the provided ViewBinding class to inflate the layout.
        Log.i("debug", "onCreateView1")
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        auth = requireNotNull(FirebaseAuth.getInstance())

        Log.i("debug", "onCreateView2")

        binding.register.setOnClickListener { registerNewUser() }

        // Return the root view.
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.register.setOnClickListener {
            registerNewUser()
        }
    }

    fun isNumeric(toCheck: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return toCheck.matches(regex)
    }

    fun sanitize_email (email: String): String {
        return email.replace(".",",")
    }

    private fun registerNewUser() {
        val email: String = binding.email.text.toString()
        val password: String = binding.password.text.toString()
        val creditcard: String = binding.creditcard.text.toString()
        Log.i("debug", email)
        Log.i("debug", password)
        if (!validator.validEmail(email)) {
            Toast.makeText(
                requireContext(),
                getString(R.string.invalid_email),
                Toast.LENGTH_LONG
            ).show()

            return
        }

        if (!validator.validPassword(password)) {
            Toast.makeText(
                requireContext(),
                getString(R.string.invalid_password),
                Toast.LENGTH_LONG
            ).show()

            return
        }

        if (!isNumeric(creditcard) || creditcard.length != 16) {
            Toast.makeText(
                requireContext(),
                getString(R.string.invalid_creditcard),
                Toast.LENGTH_LONG
            ).show()

            return
        }

        binding.progressBar.visibility = View.VISIBLE
        Log.i("registration", "adding new user in the firebase with email/creditcard info")

        /* This checks whether credit card information exists in the database, if it does, it cancels the registration */
        val firebaseDatabase = FirebaseDatabase.getInstance()
        var myRef = firebaseDatabase.getReference()


        myRef.child("credit_cards").child(creditcard).get().addOnSuccessListener {

            Log.i("registration", "value of it ${it}")

            if (it.value != null) {
                Toast.makeText(
                    requireContext(),
                    "Account already exists with this credit card",
                    Toast.LENGTH_LONG
                ).show()

                Log.i("registration", "not safe to proceed, credit card already exists")

            } else {
                Log.i("registration", "safe to proceed, credit card does not exist")

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        binding.progressBar.visibility = View.GONE
                        if (task.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.register_success_string),
                                Toast.LENGTH_LONG
                            ).show()


                            /* add the credit card to the database because it didn't exist before and registration succeeded */
                            var myRef = firebaseDatabase.getReference("credit_cards")

                            myRef.child(creditcard).setValue(sanitize_email(email))

                            /* This adds information about the current user - the time of registration, email, creditcard to the database
                            *   time is not really needed to be stored, but the class had to be created anyways for the priority queue. */
                            val userRef = firebaseDatabase.getReference("users")

                            val userInfo = User_Info((LocalDateTime.now()).toString(), email, creditcard)

                            userRef.child(sanitize_email(email)).setValue(userInfo)


                            findNavController().navigate(
                                R.id.action_RegistrationFragment_to_LoginFragment
                            )

                        } else {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.register_failed_string),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

            }


        }.addOnFailureListener{
            Log.i("registration", "call to snapshot failed")
        }



    }

}