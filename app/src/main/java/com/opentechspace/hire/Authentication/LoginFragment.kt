package com.opentechspace.hire.Authentication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.google.firebase.firestore.FirebaseFirestore
import com.opentechspace.hire.R
import com.opentechspace.hire.Repositorry.AuthenticationRepository
import com.opentechspace.hire.Repositorry.ReceivedRepository
import com.opentechspace.hire.VerifyingActivity
import com.opentechspace.hire.ViewModel.AuthenticationViewModel
import com.opentechspace.hire.ViewModel.AuthenticationViewModelFactory
import com.opentechspace.hire.ViewModel.ReceivedViewModel
import com.opentechspace.hire.ViewModel.ReceivedViewModelFactory


class LoginFragment : Fragment() {

    private lateinit var getEmail : EditText
    private lateinit var getPassword : EditText
    private lateinit var signIn : ImageView
    private lateinit var forgetPassword : TextView
    private lateinit var moveToSignUp : TextView
    private lateinit var myViewModel: AuthenticationViewModel
    private lateinit var navController: NavController
    private lateinit var registerRepository: AuthenticationRepository
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var receivedRepository: ReceivedRepository
    private lateinit var receivedViewModel: ReceivedViewModel
    private lateinit var firestore: FirebaseFirestore
    private  var name : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        registerRepository = AuthenticationRepository(firebaseAuth)
        myViewModel = ViewModelProvider(this, AuthenticationViewModelFactory(registerRepository))
            .get(AuthenticationViewModel::class.java)
        receivedRepository = ReceivedRepository(firestore)
        receivedViewModel = ViewModelProvider(requireActivity(),
            ReceivedViewModelFactory(receivedRepository))
            .get(ReceivedViewModel::class.java)
//        if(myViewModel.getCurrentUser() != null)
//        {
//            receivedViewModel.getProfileData("UsersProfile")
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moveToSignUp = view.findViewById(R.id.move_to_signUp)
        getEmail = view.findViewById(R.id.email_login)
        getPassword = view.findViewById(R.id.login_password)
        navController = Navigation.findNavController(view)
        signIn = view.findViewById(R.id.login_button)
        forgetPassword = view.findViewById(R.id.forget_password)

        moveToSignUp.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        forgetPassword.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_forgetPassword)
        }
        signIn.setOnClickListener {
            val email = getEmail.text.toString().trim()
            val password = getPassword.text.toString().trim()
            if(email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                if(password.isNotEmpty() && password.length >= 8)
                {
                    myViewModel.signIn(email, password)
                    Toast.makeText(requireContext(), "Checking Details Please Wait", Toast.LENGTH_SHORT).show()
                    myViewModel.registerUser.observe(viewLifecycleOwner, Observer {
                        if(it != null)
                        {
                            Toast.makeText(requireContext(), "Verified.....", Toast.LENGTH_SHORT).show()
//                            navController.navigate(R.id.action_loginFragment_to_verifying)
                            requireActivity().startActivity(Intent(requireContext(),VerifyingActivity::class.java))
                            requireActivity().finish()
                        }
                        else
                        {
                            Toast.makeText(requireContext(), "Password is not Correct", Toast.LENGTH_SHORT).show()
                        }
                    })
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
    }

}