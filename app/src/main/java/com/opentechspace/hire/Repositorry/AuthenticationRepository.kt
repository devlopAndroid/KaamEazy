package com.opentechspace.hire.Repositorry

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class AuthenticationRepository(val firebaseAuth: FirebaseAuth) {


    val registerLiveData : MutableLiveData<FirebaseUser> = MutableLiveData()
    var failMessage : String? = null

    suspend fun signUp( email : String,password : String){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful)
                {
                    registerLiveData.postValue(firebaseAuth.currentUser)
                }
            }
            .addOnFailureListener {
                failMessage = it.message.toString()
            }
    }

    suspend fun signIn(email: String,password: String)
    {
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful)
                {
                    Log.e("FirebaseUser",registerLiveData.toString())
                    registerLiveData.postValue(firebaseAuth.currentUser)
                }
            }.addOnFailureListener {
                failMessage = it.message.toString()
            }
    }
    suspend fun forgetPassword(email: String)
    {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if(it.isSuccessful)
                {
                    registerLiveData.postValue(firebaseAuth.currentUser)
                }
            }
            .addOnFailureListener {
                failMessage = it.message.toString()
            }
    }
   fun getCurrentUser() : FirebaseUser? {
        return firebaseAuth.currentUser
    }
    suspend fun signOut()
    {
        firebaseAuth.signOut()
    }

}