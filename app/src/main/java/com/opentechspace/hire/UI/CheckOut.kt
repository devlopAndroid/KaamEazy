package com.opentechspace.hire.UI

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.opentechspace.hire.Adapter.CustomCheckOUtAdapter
import com.opentechspace.hire.Adapter.OnDeleteListener
import com.opentechspace.hire.Model.CheckOutItemModel
import com.opentechspace.hire.R
import com.opentechspace.hire.Repositorry.DeleteRepository
import com.opentechspace.hire.Repositorry.ReceivedRepository
import com.opentechspace.hire.ViewModel.DeleteViewModel
import com.opentechspace.hire.ViewModel.DeleteViewModelFactory
import com.opentechspace.hire.ViewModel.ReceivedViewModel
import com.opentechspace.hire.ViewModel.ReceivedViewModelFactory


class CheckOut : Fragment(), OnDeleteListener {

    private lateinit var receivedRepository: ReceivedRepository
    private lateinit var receivedViewModel: ReceivedViewModel
    private lateinit var deleteViewModel : DeleteViewModel
    private lateinit var deleteRepository: DeleteRepository
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerList : RecyclerView
    private lateinit var radioGroup: RadioGroup
    private lateinit var checkoutCart : LinearLayout
    private lateinit var checkOutPrice : TextView
    private lateinit var checkOutAddress : TextView
    private lateinit var checkOutLandMark : TextView
    private lateinit var checkOutCity : TextView
    private lateinit var visitPrice : TextView
    private var RadioButtonText : String? = null
    private lateinit var checkOutFinal : TextView
    private lateinit var checkOutTotalPrice : TextView
    private lateinit var emptyCart : TextView
    private lateinit var navController: NavController
    private lateinit var checkOutAddressChange : TextView
    private lateinit var proceedToPay : AppCompatButton
    private lateinit var customCheckOUtAdapter: CustomCheckOUtAdapter
    private var totalPrice : Int = 0
    private  var totalListSize = 0
    private lateinit var firebaseStorage : FirebaseStorage

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
        deleteViewModel = ViewModelProvider(requireActivity(),DeleteViewModelFactory(deleteRepository))
            .get(DeleteViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_out, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        recyclerList = view.findViewById(R.id.checkOut_recyclerList)
        radioGroup = view.findViewById(R.id.checkOut_RadioGroup)
        checkOutPrice = view.findViewById(R.id.checkout_Price)
        visitPrice = view.findViewById(R.id.checkout_visitPrice)
        checkOutTotalPrice = view.findViewById(R.id.checkout_totalPrice)
        checkOutAddress = view.findViewById(R.id.CheckOUt_Address)
        checkOutLandMark = view.findViewById(R.id.CheckOUt_LandMark)
        checkOutCity = view.findViewById(R.id.CheckOUt_City)
        checkOutFinal = view.findViewById(R.id.CheckOUt_Final_PayOut)
        checkoutCart = view.findViewById(R.id.checkout_Cart)
        emptyCart = view.findViewById(R.id.emptyCart)
        checkOutAddressChange = view.findViewById(R.id.cart_Address_change)
        proceedToPay = view.findViewById(R.id.proceedToPay1)
        receivedViewModel.receivedCheckOutProfile.observe(viewLifecycleOwner, Observer {list->
            val listSize = list.size
            var price : Int = 0
            val params = recyclerList.layoutParams
            if(listSize>0)
            {
                emptyCart.visibility = View.INVISIBLE
                checkoutCart.visibility = View.VISIBLE
                params.height = 125*listSize
                for (element in list)
                {
                    price += element.Price!!.toInt()
                }
                totalPrice = price
                checkOutPrice.text = totalPrice.toString()
                visitPrice.text = "50"
                val newPrice = totalPrice+50
                checkOutTotalPrice.text = newPrice.toString()
                checkOutFinal.text = newPrice.toString()
                totalListSize = listSize
            }
            else
            {
                emptyCart.visibility = View.VISIBLE
                checkoutCart.visibility = View.INVISIBLE
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
                checkOutAddress.text = data.Address
                checkOutCity.text = data.City
                checkOutLandMark.text = data.LandMark
            }
        })
        deleteViewModel.deleteLiveData.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty())
            {
                Toast.makeText(requireContext(), "ItemDeleted Successfully", Toast.LENGTH_SHORT).show()
            }
        })
        checkOutAddressChange.setOnClickListener {
            navController.navigate(R.id.action_checkOut_to_changeAddress)
        }
        radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            val newRadioButton = view.findViewById<RadioButton>(checkedId)
            RadioButtonText = newRadioButton.text.toString()
        }
        proceedToPay.setOnClickListener {
            if(RadioButtonText != null)
            {
                if(RadioButtonText.equals("Cash on Service (COs)"))
                {
                    if(totalListSize>0)
                    {
                        navController.navigate(R.id.action_checkOut_to_proceedToPay)
                    }
                    else
                    {
                        Toast.makeText(requireContext(), "Cannot Proceed With empty Items", Toast.LENGTH_SHORT).show()
                    }
                }
                else
                {
                    Toast.makeText(requireContext(), "Sorry We are not accepting Online Payment", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(requireContext(), "Please Select Payment Options", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDelete(checkOutItemModel: CheckOutItemModel) {
        deleteViewModel.deleteData(checkOutItemModel)
    }

}