package com.opentechspace.hire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.opentechspace.hire.Repositorry.AuthenticationRepository
import com.opentechspace.hire.Repositorry.ReceivedRepository
import com.opentechspace.hire.ViewModel.AuthenticationViewModel
import com.opentechspace.hire.ViewModel.AuthenticationViewModelFactory
import com.opentechspace.hire.ViewModel.ReceivedViewModel
import com.opentechspace.hire.ViewModel.ReceivedViewModelFactory

class VerifyingActivity : AppCompatActivity() {
    private lateinit var myViewModel: AuthenticationViewModel
    private lateinit var registerRepository: AuthenticationRepository
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var gifImage : ImageView
    private lateinit var receivedRepository: ReceivedRepository
    private lateinit var receivedViewModel: ReceivedViewModel
    private lateinit var firestore: FirebaseFirestore
    private  var name : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifying)
        firestore = FirebaseFirestore.getInstance()
        receivedRepository = ReceivedRepository(firestore)
        receivedViewModel = ViewModelProvider(this,
            ReceivedViewModelFactory(receivedRepository))
            .get(ReceivedViewModel::class.java)
        firebaseAuth = FirebaseAuth.getInstance()
        registerRepository = AuthenticationRepository(firebaseAuth)
        myViewModel = ViewModelProvider(this, AuthenticationViewModelFactory(registerRepository))
            .get(AuthenticationViewModel::class.java)
        if(myViewModel.getCurrentUser() != null)
        {
            receivedViewModel.getProfileData("UsersProfile")
        }

        gifImage = findViewById(R.id.verifyingImage)
        Glide.with(this).asGif()
            .load(R.drawable.animation)
            .into(gifImage)
        receivedViewModel.receivedUserProfile.observe(this, Observer {
            name = if(it.isNotEmpty()) {
                val data = it[0]
                data.Name
            } else {
                null
            }

        })
        Handler(Looper.getMainLooper()).postDelayed({
            if(name != null)
            {
                startActivity(Intent(this,BottomNavigationActivity::class.java))
                finish()
//                navController.navigate(R.id.action_verifying_to_bottomNavigationActivity)
            }
            else
            {
                startActivity(Intent(this,ProfileActivity::class.java))
                finish()
//                navController.navigate(R.id.action_verifying_to_home)
            }
        },1000)
    }
}