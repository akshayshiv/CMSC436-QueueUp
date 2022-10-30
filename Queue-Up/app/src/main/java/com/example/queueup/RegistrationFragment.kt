package com.example.queueup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

import com.example.queueup.databinding.FragmentRegisterBinding


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


    private fun registerNewUser() {
        val email: String = binding.email.text.toString()
        val password: String = binding.password.text.toString()
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

        binding.progressBar.visibility = View.VISIBLE

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                binding.progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.register_success_string),
                        Toast.LENGTH_LONG
                    ).show()

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

}