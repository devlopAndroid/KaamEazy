package com.opentechspace.hire.UI

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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


class ChangeAddress : Fragment() {

    private lateinit var receivedRepository: ReceivedRepository
    private lateinit var receivedViewModel: ReceivedViewModel
    private lateinit var insertRepository: InsertRepository
    private lateinit var insertViewModel: InsertViewModel
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var storedHouseNo : TextView
    private lateinit var storedAddress : TextView
    private lateinit var storedCity : TextView
    private lateinit var storedLandMark : TextView
    private lateinit var changedHouseNo : EditText
    private lateinit var changedCity : EditText
    private lateinit var changedaddress : EditText
    private lateinit var changedLandMark : EditText
    private lateinit var changedSaveButton : TextView
    private lateinit var changedAddress_arrowBack : ImageView
    private var firebaseId : String? = null
    private var saveImage : Uri? = null
    private var name : String? = null
    private var age : String? = null
    private var email : String? = null
    private var mobile : String? = null


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
        return inflater.inflate(R.layout.fragment_change_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storedHouseNo = view.findViewById(R.id.storedHouseNo)
        storedAddress = view.findViewById(R.id.storedAddress)
        storedCity = view.findViewById(R.id.storedCity)
        storedLandMark = view.findViewById(R.id.storedLandMark)
        changedHouseNo = view.findViewById(R.id.changedHouseNo)
        changedaddress = view.findViewById(R.id.changedAddress)
        changedCity = view.findViewById(R.id.changedCityName)
        changedLandMark = view.findViewById(R.id.changedlandMark)
        changedSaveButton = view.findViewById(R.id.changedSave)
        changedAddress_arrowBack = view.findViewById(R.id.changed_ArrowBack)
        receivedViewModel.receivedUserProfile.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty())
            {
                val data = it[0]
                storedHouseNo.text = data.HouseNo
                storedAddress.text = data.Address
                storedCity.text = data.City
                storedLandMark.text = data.LandMark
                changedHouseNo.setText(data.HouseNo)
                changedaddress.setText(data.Address)
                changedCity.setText(data.City)
                changedLandMark.setText(data.LandMark)
                firebaseId = data.Id
                name = data.Name
                saveImage = data.ImageUrl?.toUri()
                age = data.Age
                mobile = data.Mobile
                email = data.Email
            }
        })


        changedSaveButton.setOnClickListener {
            val houseNo = changedHouseNo.text.toString().trim()
            val address = changedaddress.text.toString().trim()
            val city = changedCity.text.toString().trim()
            val landMark = changedLandMark.text.toString().trim()

            insertViewModel.insertUpdatedProfileData(saveImage.toString(),
                name.toString(),
                age.toString(),email.toString(),mobile.toString(),city,houseNo,address,landMark,"UsersProfile",firebaseId,1)
//            Toast.makeText(requireContext(), "Updated Successfully", Toast.LENGTH_SHORT).show()
            Log.e("data", "$saveImage,$name,$age,$city,$mobile,$houseNo,$email")
            insertViewModel.insertUpdatedProfileLiveData.observe(viewLifecycleOwner, Observer {
                if(it != null)
                {
                    Toast.makeText(requireContext(), "Updated Successfully", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_changeAddress_to_checkOut)
                }
                else
                {
                    Toast.makeText(requireContext(), "Failed to Save Data", Toast.LENGTH_SHORT).show()
                }
            })
        }
        changedAddress_arrowBack.setOnClickListener {
            findNavController().navigate(R.id.action_changeAddress_to_checkOut)
        }
    }

}