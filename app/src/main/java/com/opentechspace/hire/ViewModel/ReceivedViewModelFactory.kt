package com.opentechspace.hire.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.opentechspace.hire.Repositorry.InsertRepository
import com.opentechspace.hire.Repositorry.ReceivedRepository

class ReceivedViewModelFactory(private val receivedRepository: ReceivedRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReceivedViewModel(receivedRepository) as T
    }
}