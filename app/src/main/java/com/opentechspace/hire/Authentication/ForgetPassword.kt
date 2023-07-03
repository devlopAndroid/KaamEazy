package com.opentechspace.hire.Authentication

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.opentechspace.hire.R
import com.opentechspace.hire.Repositorry.AuthenticationRepository
import com.opentechspace.hire.ViewModel.AuthenticationViewModel
import com.opentechspace.hire.ViewModel.AuthenticationViewModelFactory


class ForgetPassword : Fragment() {

    private lateinit var getEmail : EditText
    private lateinit var buttonClick : ImageView
    private lateinit var moveToSignIn : TextView
    private lateinit var myViewModel: AuthenticationViewModel
    private lateinit var navController: NavController
    private lateinit var registerRepository: AuthenticationRepository
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        registerRepository = AuthenticationRepository(firebaseAuth)
        myViewModel = ViewModelProvider(this, AuthenticationViewModelFactory(registerRepository))
            .get(AuthenticationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forget_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moveToSignIn = view.findViewById(R.id.forget_to_login)
        getEmail = view.findViewById(R.id.forget_Email)
        navController = Navigation.findNavController(view)
        buttonClick = view.findViewById(R.id.forget_button)

        buttonClick.setOnClickListener {
            val email = getEmail.text.toString().trim()
            if(email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                myViewModel.forgetPassword(email)
                Toast.makeText(requireContext(), "Reset Link Sent to Your Verified Email", Toast.LENGTH_LONG).show()
                navController.navigate(R.id.action_forgetPassword_to_loginFragment)
            }
            else
            {
                Toast.makeText(requireContext(), "Please Enter Valid Email", Toast.LENGTH_SHORT).show()
            }
        }
        moveToSignIn.setOnClickListener {
            navController.navigate(R.id.action_forgetPassword_to_loginFragment)
        }
    }
}