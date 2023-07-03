package com.opentechspace.hire.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.opentechspace.hire.R
import com.opentechspace.hire.Repositorry.InsertRepository
import com.opentechspace.hire.VerifyingActivity
import com.opentechspace.hire.ViewModel.InsertViewModel
import com.opentechspace.hire.ViewModel.InsertViewModelFactory


class NextHome : Fragment() {

    private lateinit var name : String
    private lateinit var age : String
    private lateinit var email : String
    private lateinit var mobileNo : String
    private lateinit var imageURi : String
    private lateinit var city : EditText
    private lateinit var houseNo : EditText
    private lateinit var completeAddress : EditText
    private lateinit var landMark : EditText
    private lateinit var save : TextView
    private lateinit var previous : TextView
    private lateinit var insertRepository: InsertRepository
    private lateinit var insertViewModel: InsertViewModel
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var navController: NavController
    private lateinit var progressBar: ProgressBar

    val args : NextHomeArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        insertRepository = InsertRepository(storage,firestore)
        insertViewModel = ViewModelProvider(this,InsertViewModelFactory(insertRepository))
            .get(InsertViewModel::class.java)

        name = args.name
        age = args.age
        email = args.email
        mobileNo = args.mobile
        imageURi = args.imageUri

        Log.e("Data","$name,$age,$email,$mobileNo,$imageURi")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_next_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        city = view.findViewById(R.id.cityName)
        houseNo = view.findViewById(R.id.houseNo)
        completeAddress = view.findViewById(R.id.address)
        landMark = view.findViewById(R.id.landMark)
        save = view.findViewById(R.id.save)
        previous = view.findViewById(R.id.previousPage)
        progressBar = view.findViewById(R.id.nextHomeProgressBar)
        navController = Navigation.findNavController(view)
        previous.setOnClickListener {
            val action = NextHomeDirections.actionNextHomeToHome()
            navController.navigate(action)
        }

        save.setOnClickListener {
            val city = city.text.toString().trim()
            val houseNo = houseNo.text.toString().trim()
            val completeAddress = completeAddress.text.toString().trim()
            val landmark = landMark.text.toString().trim()
            if(city.isNotEmpty() && houseNo.isNotEmpty() && completeAddress.isNotEmpty() && landmark.isNotEmpty())
            {
                if(city != "Gwalior")
                {
                    Snackbar.make(requireView(),"Sorry for Inconvenience We are not Operating on Your City We will see in future But You can Visit Our App",
                        Snackbar.LENGTH_SHORT).show()
                }
                insertViewModel.insertProfileData(imageURi,name,age,email,mobileNo,city,houseNo,completeAddress,landmark,"UsersProfile")
//                Toast.makeText(requireContext(), "Please Wait for Moment...", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.VISIBLE
                insertViewModel.insertProfileLiveData.observe(viewLifecycleOwner, Observer {
                    if(it != null)
                    {  Toast.makeText(requireContext(), "Profile Created Successfully", Toast.LENGTH_SHORT).show()
//                        navController.navigate(R.id.action_nextHome_to_verifying)
                        requireActivity().startActivity(Intent(requireContext(),VerifyingActivity::class.java))
                    }
                    else
                    {
                        Toast.makeText(requireContext(), "Failed to Save Data", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            else
            {
                Toast.makeText(requireContext(), "Please Filled All the Fields", Toast.LENGTH_SHORT).show()
            }
        }

    }

}