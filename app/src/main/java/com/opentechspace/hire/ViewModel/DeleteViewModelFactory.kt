package com.opentechspace.hire.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.opentechspace.hire.Repositorry.DeleteRepository

class DeleteViewModelFactory(private val repository: DeleteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DeleteViewModel(repository) as T
    }
}