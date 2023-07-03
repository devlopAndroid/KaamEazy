package com.opentechspace.hire.UI

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
import com.google.firebase.firestore.FirebaseFirestore
import com.opentechspace.hire.R
import com.opentechspace.hire.Repositorry.ReceivedRepository
import com.opentechspace.hire.ViewModel.ReceivedViewModel
import com.opentechspace.hire.ViewModel.ReceivedViewModelFactory
import de.hdodenhof.circleimageview.CircleImageView


class MyProfile : Fragment() {

    private lateinit var receivedRepository: ReceivedRepository
    private lateinit var receivedViewModel: ReceivedViewModel
    private lateinit var firestore: FirebaseFirestore
    private lateinit var navController: NavController
    private lateinit var userName : TextView
    private lateinit var userPhone : TextView
    private lateinit var userAge : TextView
    private lateinit var userEmail : TextView
    private lateinit var userHouseNo : TextView
    private lateinit var userCity : TextView
    private lateinit var userAddress : TextView
    private lateinit var userPincode : TextView
    private lateinit var editProfile : LinearLayout
    private lateinit var profileImage : CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
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
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        profileImage = view.findViewById(R.id.myProfileImage)
        userName = view.findViewById(R.id.myProfileName)
        userAge = view.findViewById(R.id.myProfileAge)
        userPhone = view.findViewById(R.id.myProfileNumber)
        userEmail = view.findViewById(R.id.myProfileEmail)
        userCity = view.findViewById(R.id.myProfileCity)
        userHouseNo = view.findViewById(R.id.myProfileHouseNumber)
        userAddress = view.findViewById(R.id.myProfileAddress)
        userPincode = view.findViewById(R.id.myProfileLandMark)
        editProfile = view.findViewById(R.id.editProfile)
        receivedViewModel.receivedUserProfile.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty())
            {
                val data = it[0]
                val phone : String? = data.Mobile
                userName.text = data.Name
                userPhone.text = "+91$phone"
                Glide.with(requireContext())
                    .load(data.ImageUrl)
                    .into(profileImage)
                userAge.text = data.Age
                userEmail.text = data.Email
                userHouseNo.text = data.HouseNo
                userCity.text = data.City
                userAddress.text = data.Address
                userPincode.text = data.LandMark
            }
            else
            {
                userName.text = "UserName"
                userPhone.text = "User Mobile Number"
                userAge.text = "User Age"
                userEmail.text = "User Email"
                userHouseNo.text = "User HouseNo"
                userCity.text = "User UserCity"
                userAddress.text = "User UserAddress"
                userPincode.text = "User LandMark"
            }
        })
        editProfile.setOnClickListener {
//            childFragmentManager.beginTransaction().replace(R.id.myProfile_fragment,EditMyProfile()).commit()
            navController.navigate(R.id.action_myProfile_to_editMyProfile)
        }
    }

}