package com.opentechspace.hire.UI

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.opentechspace.hire.R
import com.opentechspace.hire.Repositorry.AuthenticationRepository
import com.opentechspace.hire.ViewModel.AuthenticationViewModel
import com.opentechspace.hire.ViewModel.AuthenticationViewModelFactory
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text

class Home : Fragment() {

    private lateinit var navController: NavController
    private lateinit var getName : EditText
    private lateinit var age : EditText
    private lateinit var email : EditText
    private lateinit var mobile : EditText
    private lateinit var nextButton : TextView
    private var imageUri : Uri? = null
    private lateinit var circularImage : CircleImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        firebaseAuth = FirebaseAuth.getInstance()
//        registerRepository = AuthenticationRepository(firebaseAuth)
//        myViewModel = ViewModelProvider(this, AuthenticationViewModelFactory(registerRepository))
//            .get(AuthenticationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        logOut = view.findViewById(R.id.logOUt)
        nextButton = view.findViewById(R.id.nextPage)
        navController = Navigation.findNavController(view)
//        logOut.setOnClickListener {
//            myViewModel.signOut()
//            navController.navigate(R.id.action_home_to_loginFragment)
//        }
        getName = view.findViewById(R.id.name)
        age = view.findViewById(R.id.age)
        email = view.findViewById(R.id.email)
        mobile = view.findViewById(R.id.mobile)
        circularImage = view.findViewById(R.id.profile_image)

        circularImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            ImagePick.launch(galleryIntent)
        }

        nextButton.setOnClickListener {
            val name = getName.text.toString().trim()
            val age = age.text.toString().trim()
            val email = email.text.toString().trim()
            val mobile = mobile.text.toString().trim()

            if(name.isNotEmpty() && age.isNotEmpty() && email.isNotEmpty() && mobile.isNotEmpty())
            {
                if(imageUri != null)
                {
                    val action = HomeDirections.actionHomeToNextHome(name,age,email,mobile,imageUri.toString())
                    navController.navigate(action)
//
                }
                else
                {
                    Toast.makeText(requireContext(), "Please Select Image", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(requireContext(), "Please Fill all the Fields", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private val ImagePick : ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result->

            if(result != null)
            {
                 imageUri = result.data?.data
                 circularImage.setImageURI(imageUri)
            }
            else
            {
                Toast.makeText(requireContext(), "Please select image", Toast.LENGTH_SHORT).show()
            }
        }
}