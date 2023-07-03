package com.opentechspace.hire.UI

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.opentechspace.hire.AuthenticaitonActivity
import com.opentechspace.hire.R
import com.opentechspace.hire.Repositorry.AuthenticationRepository
import com.opentechspace.hire.Repositorry.ReceivedRepository
import com.opentechspace.hire.ViewModel.AuthenticationViewModel
import com.opentechspace.hire.ViewModel.AuthenticationViewModelFactory
import com.opentechspace.hire.ViewModel.ReceivedViewModel
import com.opentechspace.hire.ViewModel.ReceivedViewModelFactory
import de.hdodenhof.circleimageview.CircleImageView


class Account : Fragment() {


    private lateinit var myViewModel: AuthenticationViewModel
    private lateinit var registerRepository: AuthenticationRepository
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var receivedRepository: ReceivedRepository
    private lateinit var receivedViewModel: ReceivedViewModel
    private lateinit var firestore: FirebaseFirestore
    private lateinit var navController: NavController
    private lateinit var userName : TextView
    private lateinit var userPhone : TextView
    private lateinit var accountImage : CircleImageView
    private lateinit var myProfile : LinearLayout
    private lateinit var helpAndSupport : LinearLayout
    private lateinit var share : LinearLayout
    private lateinit var aboutUs : LinearLayout
    private lateinit var logOut : LinearLayout

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
        receivedViewModel.getProfileData("UsersProfile")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        userName = view.findViewById(R.id.user_Name)
        userPhone = view.findViewById(R.id.user_number)
        accountImage = view.findViewById(R.id.account_image)
        myProfile = view.findViewById(R.id.myProfile)
        helpAndSupport = view.findViewById(R.id.help)
        share = view.findViewById(R.id.sharing)
        aboutUs = view.findViewById(R.id.aboutUs)
        logOut = view.findViewById(R.id.logOut)

        receivedViewModel.receivedUserProfile.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty())
            {
              val data = it[0]
              val phone : String? = data.Mobile
              userName.text = data.Name
              userPhone.text = "+91$phone"
              Glide.with(requireContext())
                  .load(data.ImageUrl)
                  .into(accountImage)
            }
            else
            {
                userName.text = "UserName"
                userPhone.text = "User Mobile Number"
            }
        })
        myProfile.setOnClickListener {
//           childFragmentManager.beginTransaction().addToBackStack(Account().toString()).replace(R.id.account_fragment,MyProfile()).commit()
        //            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.account_fragment,MyProfile())?.addToBackStack("null")
//                ?.commit()
            navController.navigate(R.id.action_account_to_myProfile)
//         navController.navigate(R.id.action_bottomNavigationFragment_to_myProfile)
        }
        logOut.setOnClickListener {
            myViewModel.signOut()
            requireActivity().startActivity(Intent(requireContext(),AuthenticaitonActivity::class.java))
            requireActivity().finish()
        }
    }
}