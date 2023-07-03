package com.opentechspace.hire.UI

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.opentechspace.hire.Adapter.CustomCheckOUtAdapter
import com.opentechspace.hire.Adapter.OnDeleteListener
import com.opentechspace.hire.Model.CheckOutItemModel
import com.opentechspace.hire.R
import com.opentechspace.hire.Repositorry.DeleteRepository
import com.opentechspace.hire.Repositorry.InsertRepository
import com.opentechspace.hire.Repositorry.ReceivedRepository
import com.opentechspace.hire.ViewModel.*


class ProceedToPay : Fragment(), OnDeleteListener {

    private lateinit var receivedRepository: ReceivedRepository
    private lateinit var receivedViewModel: ReceivedViewModel
    private lateinit var insertRepository: InsertRepository
    private lateinit var insertViewModel: InsertViewModel
    private lateinit var deleteViewModel : DeleteViewModel
    private lateinit var deleteRepository: DeleteRepository
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseStorage : FirebaseStorage
    private lateinit var customCheckOUtAdapter: CustomCheckOUtAdapter
    private lateinit var navController: NavController
    private lateinit var userName : TextView
    private lateinit var userPhoneNo : TextView
    private lateinit var recyclerList : RecyclerView
    private lateinit var orderedTotalItems : TextView
    private lateinit var orderedTotalPrice : TextView
    private lateinit var orderedHouseNo : TextView
    private lateinit var orderedAddress : TextView
    private lateinit var orderedCity : TextView
    private lateinit var orderedLandMark : TextView
    private lateinit var placeOrderedButton : AppCompatButton
    private lateinit var cancelButton : AppCompatButton
    private lateinit var firebaseAuth: FirebaseAuth
    private var totalListSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        receivedRepository = ReceivedRepository(firestore)
        receivedViewModel = ViewModelProvider(requireActivity(),
            ReceivedViewModelFactory(receivedRepository))
            .get(ReceivedViewModel::class.java)
        receivedViewModel.getProfileData("UsersProfile")
        receivedViewModel.getCheckOutData("UsersProfile")
        deleteRepository = DeleteRepository(firestore,firebaseStorage)
        deleteViewModel = ViewModelProvider(requireActivity(),
            DeleteViewModelFactory(deleteRepository))
            .get(DeleteViewModel::class.java)
        insertRepository = InsertRepository(firebaseStorage,firestore)
        insertViewModel = ViewModelProvider(this, InsertViewModelFactory(insertRepository))
            .get(InsertViewModel::class.java)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_proceed_to_pay, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        recyclerList = view.findViewById(R.id.ordered_recyclerList)
        userName = view.findViewById(R.id.order_UserName)
        userPhoneNo = view.findViewById(R.id.order_Mobile_No)
        orderedTotalItems = view.findViewById(R.id.ordered_totalItems)
        orderedTotalPrice = view.findViewById(R.id.ordered_totalPrice)
        orderedHouseNo = view.findViewById(R.id.orderedHouseNo)
        orderedAddress = view.findViewById(R.id.orderedAddress)
        orderedCity = view.findViewById(R.id.orderedCity)
        orderedLandMark = view.findViewById(R.id.orderedLandMark)
        placeOrderedButton = view.findViewById(R.id.place_Ordered)
        cancelButton = view.findViewById(R.id.cancel_Ordered)

        receivedViewModel.receivedCheckOutProfile.observe(viewLifecycleOwner, Observer {list->
            val listSize = list.size
            var price : Int = 0
            totalListSize = listSize
            val params = recyclerList.layoutParams
            if(listSize>0)
            {
                params.height = 125*listSize
                for (element in list)
                {
                    price += element.Price!!.toInt()
                }
                val newPrice = price+50
                orderedTotalItems.text = listSize.toString()
                orderedTotalPrice.text = newPrice.toString()
            }
            else
            {
                params.height = 0
                orderedTotalItems.text = "0"
                orderedTotalPrice.text = "0"
                Toast.makeText(requireContext(), "To Proceed To Pay You should have atleast one Items", Toast.LENGTH_SHORT).show()
            }
            recyclerList.layoutParams = params
            recyclerList.setHasFixedSize(true)
            recyclerList.layoutManager = LinearLayoutManager(requireContext())
            customCheckOUtAdapter = CustomCheckOUtAdapter(requireContext(),list,this)
            recyclerList.adapter = customCheckOUtAdapter
            customCheckOUtAdapter.notifyDataSetChanged()
        })
        receivedViewModel.receivedUserProfile.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                val data = it[0]
                val mobileNo = "+91${data.Mobile}"
               userName.text = data.Name
               userPhoneNo.text = mobileNo
               orderedHouseNo.text = data.HouseNo
               orderedAddress.text = data.Address
               orderedCity.text = data.City
               orderedLandMark.text = data.LandMark
            }
        })
        deleteViewModel.deleteLiveData.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty())
            {
                Toast.makeText(requireContext(), "ItemDeleted Successfully", Toast.LENGTH_SHORT).show()
            }
        })
        placeOrderedButton.setOnClickListener {
            if(totalListSize>0)
            {
                if(orderedTotalPrice.text.isNotEmpty())
                {
                    if(orderedHouseNo.text.isNotEmpty() && orderedAddress.text.isNotEmpty() && orderedCity.text.isNotEmpty())
                    {
                        insertViewModel.insertDoneData(firebaseAuth.currentUser?.uid.toString(),"DoneData")
                        insertViewModel.insertDoneLiveData.observe(viewLifecycleOwner, Observer {
                            if(it != null)
                            {
                                navController.navigate(R.id.action_proceedToPay_to_done)
                            }
                            else
                            {
                                Toast.makeText(requireContext(), "Failed to Booked Services Please Try Again", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    else
                    {
                        Toast.makeText(requireContext(), "Your address details is empty We Cannot move Forward", Toast.LENGTH_SHORT).show()
                    }
                }
                else
                {
                    Toast.makeText(requireContext(), "Price is not Valid", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(requireContext(), "To Proceed To Pay You should have atleast one Item", Toast.LENGTH_SHORT).show()
            }
        }
        cancelButton.setOnClickListener {
            navController.navigate(R.id.action_proceedToPay_to_home)
        }
    }

    override fun onDelete(checkOutItemModel: CheckOutItemModel) {
        deleteViewModel.deleteData(checkOutItemModel)
    }
}