package com.opentechspace.hire.Authentication

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.opentechspace.hire.R
import com.opentechspace.hire.Repositorry.AuthenticationRepository
import com.opentechspace.hire.ViewModel.AuthenticationViewModel
import com.opentechspace.hire.ViewModel.AuthenticationViewModelFactory


class SignUpFragment : Fragment() {

    private lateinit var getEmail : EditText
    private lateinit var getPassword : EditText
    private lateinit var getConfirmPasword : EditText
    private lateinit var register : ImageView
    private lateinit var alreadyAccount : TextView
    private lateinit var myViewModel: AuthenticationViewModel
    private lateinit var navController: NavController
    private lateinit var registerRepository: AuthenticationRepository
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        registerRepository = AuthenticationRepository(firebaseAuth)

        myViewModel = ViewModelProvider(this,AuthenticationViewModelFactory(registerRepository))
            .get(AuthenticationViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getEmail = view.findViewById(R.id.input_email)
        getPassword = view.findViewById(R.id.input_password)
        getConfirmPasword = view.findViewById(R.id.confirm_password)
        navController = Navigation.findNavController(view)
        register = view.findViewById(R.id.SignUp_button)
        alreadyAccount = view.findViewById(R.id.move_to_signIn)




        register.setOnClickListener {
            val email = getEmail.text.toString().trim()
            Log.e("Email",email.toString())
            val password = getPassword.text.toString().trim()
            val confirmPassword = getConfirmPasword.text.toString().trim()
            if(email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                if(password.isNotEmpty() && password.length >= 8)
                {

                    if(password == confirmPassword)
                    {
                        myViewModel.signUp(email, password)
                        Toast.makeText(requireContext(), "Successfully Register", Toast.LENGTH_SHORT).show()
                        myViewModel.registerUser.observe(viewLifecycleOwner, Observer {
                            if(it != null)
                            {
                                navController.navigate(R.id.action_signUpFragment_to_loginFragment)
                            }
                            else
                            {
                                Toast.makeText(requireContext(), "Please check Your Internet", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    else
                    {
                        Toast.makeText(requireContext(), "Both Password are not Matching", Toast.LENGTH_SHORT).show()
                    }
                }
                else
                {
                    Toast.makeText(requireContext(), "Password Size is too Small", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(requireContext(), "Please Enter Valid Email", Toast.LENGTH_SHORT).show()
            }
        }
        alreadyAccount.setOnClickListener {
            navController.navigate(R.id.action_signUpFragment_to_loginFragment)
        }
    }
}