package com.opentechspace.hire.UI

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.opentechspace.hire.Adapter.CustomNextAdapter
import com.opentechspace.hire.Adapter.OnAddData
import com.opentechspace.hire.R
import com.opentechspace.hire.Repositorry.InsertRepository
import com.opentechspace.hire.Repositorry.ReceivedRepository
import com.opentechspace.hire.ViewModel.InsertViewModel
import com.opentechspace.hire.ViewModel.InsertViewModelFactory
import com.opentechspace.hire.ViewModel.ReceivedViewModel
import com.opentechspace.hire.ViewModel.ReceivedViewModelFactory
import org.w3c.dom.Text

class ServiceFragment : Fragment(), OnAddData {

    private lateinit var receivedRepository: ReceivedRepository
    private lateinit var receivedViewModel: ReceivedViewModel
    private lateinit var firestore: FirebaseFirestore
    private lateinit var insertRepository: InsertRepository
    private lateinit var insertViewModel: InsertViewModel
    private lateinit var storage: FirebaseStorage
    private lateinit var getTitle : String
    private var arrowBackValue : Int = 0
    private lateinit var originalTitle : String
    private lateinit var serviceList : RecyclerView
    private lateinit var arrowBack : ImageView
    private lateinit var customNextAdapter: CustomNextAdapter
    private lateinit var navController: NavController
    private lateinit var toolbar : Toolbar
    private var initial : Int = 1
    val args : ServiceFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getTitle = args.title1
        arrowBackValue = args.arrowBack
        originalTitle = args.originalTitle
        Log.e("NextTitle",getTitle.toString())
        firestore = FirebaseFirestore.getInstance()
        receivedRepository = ReceivedRepository(firestore)
        receivedViewModel = ViewModelProvider(requireActivity(),
            ReceivedViewModelFactory(receivedRepository))
            .get(ReceivedViewModel::class.java)
        receivedViewModel.getNextData(getTitle)
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
        return inflater.inflate(R.layout.fragment_service, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        arrowBack = view.findViewById(R.id.service_arrowBack)
        toolbar = view.findViewById(R.id.toolbar2)
        serviceList = view.findViewById(R.id.service_List)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.title = "$getTitle Services"
        }
        arrowBack.setOnClickListener {
            if(arrowBackValue == 0)
            {
                navController.navigate(R.id.action_serviceFragment_to_home)
            }
            else
            {
                val action = ServiceFragmentDirections.actionServiceFragmentToSeeAll(originalTitle)
                navController.navigate(action)
            }
        }
        receivedViewModel.receivedNextStoredData.observe(viewLifecycleOwner, Observer {
            serviceList.setHasFixedSize(true)
            serviceList.layoutManager = LinearLayoutManager(requireContext())
            customNextAdapter = CustomNextAdapter(requireContext(),it,this)
            serviceList.adapter = customNextAdapter
            customNextAdapter.notifyDataSetChanged()
        })

    }

    override fun onAddData(title: String, price: String, imageUrl: String) {
//        Toast.makeText(requireContext(), "onAddClicked", Toast.LENGTH_SHORT).show()
        val bottomSheet = BottomSheetDialog(requireContext(),R.style.BottomSheetDialogTheme)
        bottomSheet.setContentView(R.layout.customviewcart)
        bottomSheet.show()
        val itemNumber = bottomSheet.findViewById<TextView>(R.id.itemNumber)
        val viewCart = bottomSheet.findViewById<TextView>(R.id.viewCart)
        if (itemNumber != null) {
            itemNumber.text = initial++.toString()
            insertViewModel.insertCheckoutProfileData(imageUrl,title,price,"UsersProfile")
            insertViewModel.insertCheckoutProfileLiveData.observe(viewLifecycleOwner, Observer {
                Toast.makeText(requireContext(), "Item Saved Successfully", Toast.LENGTH_SHORT).show()
            })
        }
        viewCart!!.setOnClickListener {
            navController.navigate(R.id.action_serviceFragment_to_checkOut)
            bottomSheet.dismiss()
        }
    }
}