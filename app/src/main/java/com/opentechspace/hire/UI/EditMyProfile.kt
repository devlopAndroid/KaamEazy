package com.opentechspace.hire.UI

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.opentechspace.hire.R
import com.opentechspace.hire.Repositorry.InsertRepository
import com.opentechspace.hire.Repositorry.ReceivedRepository
import com.opentechspace.hire.ViewModel.InsertViewModel
import com.opentechspace.hire.ViewModel.InsertViewModelFactory
import com.opentechspace.hire.ViewModel.ReceivedViewModel
import com.opentechspace.hire.ViewModel.ReceivedViewModelFactory
import de.hdodenhof.circleimageview.CircleImageView


class EditMyProfile : Fragment() {

    private lateinit var receivedRepository: ReceivedRepository
    private lateinit var receivedViewModel: ReceivedViewModel
    private lateinit var insertRepository: InsertRepository
    private lateinit var insertViewModel: InsertViewModel
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var save : AppCompatButton
    private lateinit var navController: NavController
    private lateinit var userName : EditText
    private lateinit var userPhone : EditText
    private lateinit var userAge : EditText
    private lateinit var userEmail : EditText
    private lateinit var userHouseNo : EditText
    private lateinit var userCity : EditText
    private lateinit var userAddress : EditText
    private lateinit var userPincode : EditText
    private lateinit var profileImage : CircleImageView
    private var firebaseId : String? = null
    private var imageUri : Uri? = null
    private var saveImage : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        receivedRepository = ReceivedRepository(firestore)
        receivedViewModel = ViewModelProvider(requireActivity(),
            ReceivedViewModelFactory(receivedRepository))
            .get(ReceivedViewModel::class.java)
        receivedViewModel.getProfileData("UsersProfile")
        storage = FirebaseStorage.getInstance()
        insertRepository = InsertRepository(storage,firestore)
        insertViewModel = ViewModelProvider(this, InsertViewModelFactory(insertRepository))
            .get(InsertViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_my_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        profileImage = view.findViewById(R.id.editProfileImage)
        userName = view.findViewById(R.id.editProfileName)
        userAge = view.findViewById(R.id.editProfileAge)
        userPhone = view.findViewById(R.id.editPhoneNo)
        userEmail = view.findViewById(R.id.editProfileEmail)
        userCity = view.findViewById(R.id.editProfileCity)
        userHouseNo = view.findViewById(R.id.editProfileHouseNo)
        userAddress = view.findViewById(R.id.editProfileAddress)
        userPincode = view.findViewById(R.id.editProfilePincode)
        save = view.findViewById(R.id.updateProfile)
        receivedViewModel.receivedUserProfile.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty())
            {
                val data = it[0]
//                val phone : String? = data.Mobile
                userName.setText(data.Name)
                firebaseId = data.Id
                userPhone.setText(data.Mobile)
                Glide.with(requireContext())
                    .load(data.ImageUrl)
                    .into(profileImage)
                saveImage = data.ImageUrl?.toUri()
                userAge.setText(data.Age)
                userEmail.setText(data.Email)
                userHouseNo.setText(data.HouseNo)
                userCity.setText(data.City)
                userAddress.setText(data.Address)
                userPincode.setText(data.LandMark)
            }
        })
        profileImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            ImagePick.launch(galleryIntent)
        }
        save.setOnClickListener {
            val name = userName.text.toString().trim()
            val age = userAge.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val mobile = userPhone.text.toString().trim()
            val city = userCity.text.toString().trim()
            val houseNo = userHouseNo.text.toString().trim()
            val completeAddress = userAddress.text.toString().trim()
            val landmark = userPincode.text.toString().trim()


            if (imageUri == null) {
                insertViewModel.insertUpdatedProfileData(saveImage.toString(),name,age,email,mobile,city,houseNo,completeAddress,landmark,"UsersProfile",firebaseId,1)
                Toast.makeText(requireContext(), "Please Wait...", Toast.LENGTH_SHORT).show()
                Log.e("data", "$saveImage,$name,$age,$city,$mobile,$houseNo,$email")
                insertViewModel.insertUpdatedProfileLiveData.observe(viewLifecycleOwner, Observer {
                    if(it != null)
                    {
                        navController.navigate(R.id.action_editMyProfile_to_myProfile)
                        Toast.makeText(requireContext(), "Updated Successfully", Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        Toast.makeText(requireContext(), "Failed to Save Data", Toast.LENGTH_SHORT).show()
                    }
                })
            } else
            {
                insertViewModel.insertUpdatedProfileData(imageUri.toString(),name,age,email,mobile,city,houseNo,completeAddress,landmark,"UsersProfile",firebaseId,2)
                 Toast.makeText(requireContext(), "Please Wait...", Toast.LENGTH_SHORT).show()
                Log.e("data", "$imageUri,$name,$age,$city,$mobile,$houseNo,$email")
                insertViewModel.insertUpdatedProfileLiveData.observe(viewLifecycleOwner, Observer {
                    if(it != null)
                    {
                        navController.navigate(R.id.action_editMyProfile_to_myProfile)
                        Toast.makeText(requireContext(), "Updated Successfully", Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        Toast.makeText(requireContext(), "Failed to Save Data", Toast.LENGTH_SHORT).show()
                    }
                })
            }

        }




    }
    private val ImagePick : ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result->

            if(result != null)
            {
                imageUri = result.data?.data
                profileImage.setImageURI(imageUri)
            }
            else
            {
                Toast.makeText(requireContext(), "Please select image", Toast.LENGTH_SHORT).show()
            }
        }
}