package com.opentechspace.hire.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.opentechspace.hire.Repositorry.AuthenticationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthenticationViewModel(val authenticationRepository: AuthenticationRepository) : ViewModel() {

    val registerUser : MutableLiveData<FirebaseUser> = authenticationRepository.registerLiveData
    val failMessage : String? = authenticationRepository.failMessage

    fun signUp(email : String ,password : String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepository.signUp(email, password)
        }
    }
    fun signIn(email: String,password: String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepository.signIn(email, password)
        }

    }

    fun forgetPassword(email: String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepository.forgetPassword(email)
        }
    }
    fun getCurrentUser() : FirebaseUser?
    {
        return authenticationRepository.getCurrentUser()
    }
    fun signOut(){
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepository.signOut()
        }
    }

}