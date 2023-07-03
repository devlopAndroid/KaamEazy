package com.opentechspace.hire.UI

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.opentechspace.hire.Adapter.*
import com.opentechspace.hire.R
import com.opentechspace.hire.Repositorry.ReceivedRepository
import com.opentechspace.hire.ViewModel.ReceivedViewModel
import com.opentechspace.hire.ViewModel.ReceivedViewModelFactory
import kotlin.math.abs


class HomeScreen : Fragment(), OnItemClickListener, OnPlumbersItemClickListener,
    OnCleaningItemClickListener, OnSalonItemClickListener, OnDeliveryItemClickListener {

    private lateinit var receivedRepository: ReceivedRepository
    private lateinit var receivedViewModel: ReceivedViewModel
    private lateinit var firestore: FirebaseFirestore
    private lateinit var myAdapter : SlidingImageAdapter
    private lateinit var customAdapter: CustomAdapter
    private lateinit var plumberAdapter: PlumberAdapter
    private lateinit var cleaningAdapter: CleaningAdapter
    private lateinit var salonAdapter: SalonAdapter
    private lateinit var navController : NavController
    private lateinit var deliveryAdapter: DeliveryAdapter
    private lateinit var viewPager2: ViewPager2
    private lateinit var handler : Handler
    private lateinit var electrician_recyclerList : RecyclerView
    private lateinit var plumber_recyclerList : RecyclerView
    private lateinit var cleaningList : RecyclerView
    private lateinit var salonList : RecyclerView
    private lateinit var deliveryList : RecyclerView
    private lateinit var eSeeAll : TextView
    private lateinit var pSeeAll : TextView
    private lateinit var cSeeAll : TextView
    private lateinit var sSeeAll : TextView
    private lateinit var dSeeAll : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
        receivedRepository = ReceivedRepository(firestore)
        receivedViewModel = ViewModelProvider(requireActivity(),ReceivedViewModelFactory(receivedRepository))
            .get(ReceivedViewModel::class.java)
        receivedViewModel.getSlidingImage("SlidingImage")
        receivedViewModel.getData("Electrician")
        receivedViewModel.getPlumberData("Plumbers")
        receivedViewModel.getCleaningData("Cleaning")
        receivedViewModel.getSalonData("Salon")
        receivedViewModel.getDeliveryData("Delivery")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_screen, container, false)
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewPager2 = view.findViewById(R.id.viewPager2)
        handler = Handler(Looper.getMainLooper())
        eSeeAll = view.findViewById(R.id.electrician_seeAll)
        pSeeAll = view.findViewById(R.id.plumber_seeAll)
        cSeeAll = view.findViewById(R.id.cleaning_seeAll)
        sSeeAll = view.findViewById(R.id.salon_seeAll)
        dSeeAll = view.findViewById(R.id.delivery_seeAll)
        electrician_recyclerList = view.findViewById(R.id.electrician_recyclerList)
        plumber_recyclerList = view.findViewById(R.id.plumber_recyclerList)
        cleaningList = view.findViewById(R.id.cleaning_recyclerList)
        salonList = view.findViewById(R.id.salon_recyclerList)
        deliveryList = view.findViewById(R.id.delivery_recyclerList)
        receivedViewModel.slidingImageData.observe(viewLifecycleOwner, Observer {

            myAdapter = SlidingImageAdapter(requireContext(),it,viewPager2)
            viewPager2.adapter = myAdapter
            viewPager2.offscreenPageLimit = 2
            viewPager2.clipToPadding = false
            viewPager2.clipChildren = false
            viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setupTransformer()
            viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    handler.removeCallbacks(runnable)
                    handler.postDelayed(runnable,2000)
                }
            })
        })
        receivedViewModel.receivedStoredData.observe(viewLifecycleOwner, Observer {
            electrician_recyclerList.setHasFixedSize(true)
            electrician_recyclerList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            customAdapter = CustomAdapter(requireContext(),it,this)
            electrician_recyclerList.adapter = customAdapter
            customAdapter.notifyDataSetChanged()
        })
        receivedViewModel.receivedPlumberData.observe(viewLifecycleOwner, Observer {
            plumber_recyclerList.setHasFixedSize(true)
            plumber_recyclerList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            plumberAdapter = PlumberAdapter(requireContext(),it,this)
            plumber_recyclerList.adapter = plumberAdapter
            plumberAdapter.notifyDataSetChanged()
        })
        receivedViewModel.receivedCleaningData.observe(viewLifecycleOwner, Observer {
            cleaningList.setHasFixedSize(true)
            cleaningList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            cleaningAdapter = CleaningAdapter(requireContext(),it,this)
            cleaningList.adapter = cleaningAdapter
            cleaningAdapter.notifyDataSetChanged()
        })
        receivedViewModel.receivedSalonData.observe(viewLifecycleOwner, Observer {
            salonList.setHasFixedSize(true)
            salonList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            salonAdapter = SalonAdapter(requireContext(),it,this)
            salonList.adapter = salonAdapter
            salonAdapter.notifyDataSetChanged()
        })
        receivedViewModel.receivedDeliveryData.observe(viewLifecycleOwner, Observer {
            deliveryList.setHasFixedSize(true)
            deliveryList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            deliveryAdapter = DeliveryAdapter(requireContext(),it,this)
            deliveryList.adapter = deliveryAdapter
            deliveryAdapter.notifyDataSetChanged()
        })
        eSeeAll.setOnClickListener {
            val action = HomeScreenDirections.actionHomeToSeeAll("Electrician")
            navController.navigate(action)
        }
        pSeeAll.setOnClickListener {
            val action = HomeScreenDirections.actionHomeToSeeAll("Plumbers")
            navController.navigate(action)
        }
        cSeeAll.setOnClickListener {
            val action = HomeScreenDirections.actionHomeToSeeAll("Cleaning")
            navController.navigate(action)
        }
        sSeeAll.setOnClickListener {
            val action = HomeScreenDirections.actionHomeToSeeAll("Salon")
            navController.navigate(action)
        }
        dSeeAll.setOnClickListener {
            val action = HomeScreenDirections.actionHomeToSeeAll("Delivery")
            navController.navigate(action)
        }

    }
    private val runnable = Runnable {
       viewPager2.currentItem = viewPager2.currentItem+1
    }
    private fun setupTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(30))
        transformer.addTransformer { page, position ->
            val r = 1- abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }
       viewPager2.setPageTransformer(transformer)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable,2000)
    }

    override fun onClickItem(title: String) {
       val action = HomeScreenDirections.actionHomeToServiceFragment(title,0,"Electrician")
        navController.navigate(action)
    }

    override fun onPlumberClickItem(title: String) {
        val action = HomeScreenDirections.actionHomeToServiceFragment(title,0,"Plumbers")
        navController.navigate(action)
    }

    override fun onCleaningClickItem(title: String) {
        val action = HomeScreenDirections.actionHomeToServiceFragment(title,0,"Cleaning")
        navController.navigate(action)
    }

    override fun onSalonClickItem(title: String) {
        val action = HomeScreenDirections.actionHomeToServiceFragment(title,0,"Salon")
        navController.navigate(action)
    }

    override fun onDeliveryClickItem(title: String) {
        val action = HomeScreenDirections.actionHomeToServiceFragment(title,0,"Delivery")
        navController.navigate(action)
    }
}