package com.opentechspace.hire.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.opentechspace.hire.Repositorry.InsertRepository

class InsertViewModelFactory(private val insertRepository: InsertRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InsertViewModel(insertRepository) as T
    }
}