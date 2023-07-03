package com.opentechspace.hire.UI

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgs
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.opentechspace.hire.Adapter.OnItemSeeAllClickListener
import com.opentechspace.hire.Adapter.SeeAllAdapter
import com.opentechspace.hire.R
import com.opentechspace.hire.Repositorry.ReceivedRepository
import com.opentechspace.hire.ViewModel.ReceivedViewModel
import com.opentechspace.hire.ViewModel.ReceivedViewModelFactory


class SeeAll : Fragment(), OnItemSeeAllClickListener {

    private lateinit var receivedRepository: ReceivedRepository
    private lateinit var receivedViewModel: ReceivedViewModel
    private lateinit var firestore: FirebaseFirestore
    private lateinit var navController : NavController
    private lateinit var seeAllAdapter: SeeAllAdapter
    private lateinit var seeList : RecyclerView
    private lateinit var getTitle : String
    private lateinit var arrowBack : ImageView

   val args : SeeAllArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getTitle = args.title
       firestore = FirebaseFirestore.getInstance()
        receivedRepository = ReceivedRepository(firestore)
        receivedViewModel = ViewModelProvider(requireActivity(),
            ReceivedViewModelFactory(receivedRepository))
            .get(ReceivedViewModel::class.java)
        receivedViewModel.getSeeAllData(getTitle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_see_all, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrowBack = view.findViewById(R.id.seeAll_arrowBack)
        seeList = view.findViewById(R.id.seeAll_List)
        navController = Navigation.findNavController(view)
        arrowBack.setOnClickListener {
            navController.navigate(R.id.action_seeAll_to_home)
        }
        receivedViewModel.receivedSeeAllData.observe(viewLifecycleOwner, Observer {
            seeList.setHasFixedSize(true)
            seeList.layoutManager = GridLayoutManager(requireContext(),2)
            seeAllAdapter = SeeAllAdapter(requireContext(),it,this)
            seeList.adapter = seeAllAdapter
            seeAllAdapter.notifyDataSetChanged()
        })
        arrowBack.setOnClickListener {
            findNavController().navigate(R.id.action_seeAll_to_home)
        }
    }

    override fun onSeeAllClickItem(title: String?) {
        val action = SeeAllDirections.actionSeeAllToServiceFragment(title!!,1,getTitle)
        navController.navigate(action)
    }
}