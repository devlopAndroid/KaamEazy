package com.opentechspace.hire.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opentechspace.hire.Model.CheckOutItemModel
import com.opentechspace.hire.Repositorry.DeleteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeleteViewModel(val repository: DeleteRepository) : ViewModel() {

    val deleteLiveData = repository.deleteItemLiveData

    fun deleteData(checkOutItemModel: CheckOutItemModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCheckOut(checkOutItemModel)
        }
    }
}