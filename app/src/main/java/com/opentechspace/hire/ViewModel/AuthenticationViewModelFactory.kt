package com.opentechspace.hire.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.opentechspace.hire.Repositorry.AuthenticationRepository

class AuthenticationViewModelFactory(val authenticationRepository: AuthenticationRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthenticationViewModel(authenticationRepository) as T
    }

}