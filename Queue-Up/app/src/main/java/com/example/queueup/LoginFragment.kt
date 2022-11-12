package com.example.queueup

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.Fragment
import com.example.queueup.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Use the provided ViewBinding class to inflate the layout.
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        Log.i("debug", "onLoginFragment")

        firebaseAuth = requireNotNull(FirebaseAuth.getInstance())

        binding.register.setOnClickListener {
            findNavController().navigate(
                R.id.action_LoginFragment_to_RegistrationFragment
            )
        }

        binding.login.setOnClickListener {
            loginUserAccount()
        }

        // Return the root view.
        return binding.root
    }

    private fun loginUserAccount() {
        val email: String = binding.email.text.toString()
        val password: String = binding.password.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(
                requireContext(),
                getString(R.string.login_toast),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(
                requireContext(),
                getString(R.string.password_toast),
                Toast.LENGTH_LONG
            ).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                binding.progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Login successful!",
                        Toast.LENGTH_LONG
                    ).show()

                    findNavController().navigate(
                        R.id.action_LoginFragment_to_RegistrationFragment // change this to something else
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Login failed! Please try again later",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

}