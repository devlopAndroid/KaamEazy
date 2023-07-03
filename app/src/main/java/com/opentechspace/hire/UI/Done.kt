package com.opentechspace.hire.UI

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
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


class Done : Fragment() {

    private lateinit var insertRepository: InsertRepository
    private lateinit var insertViewModel: InsertViewModel
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var donButton : AppCompatButton
    private lateinit var doneAnimation : ImageView
    private lateinit var doneProgressBar: ProgressBar
    private lateinit var doneText : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
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
        return inflater.inflate(R.layout.fragment_done, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        donButton = view.findViewById(R.id.donButton)
        doneAnimation = view.findViewById(R.id.doneAnimation)
        doneText = view.findViewById(R.id.doneText)
        doneProgressBar = view.findViewById(R.id.doneProgressBar)

        Glide.with(requireContext())
            .asGif()
            .load(R.drawable.donework)
            .into(doneAnimation)

        doneProgressBar.visibility = View.INVISIBLE
        doneText.visibility = View.VISIBLE
        donButton.visibility = View.VISIBLE

        donButton.setOnClickListener {
            findNavController().navigate(R.id.action_done_to_home)
        }
    }

}