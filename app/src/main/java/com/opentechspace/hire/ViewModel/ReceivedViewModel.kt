package com.opentechspace.hire.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opentechspace.hire.Repositorry.InsertRepository
import com.opentechspace.hire.Repositorry.ReceivedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReceivedViewModel(private val receivedRepository: ReceivedRepository) : ViewModel() {

    val slidingImageData = receivedRepository.slidingImageLiveData
    val receivedStoredData = receivedRepository.receivedData
    val receivedPlumberData = receivedRepository.receivedPlumberData
    val receivedCleaningData = receivedRepository.receivedCleaningData
    val receivedSalonData = receivedRepository.receivedSalonData
    val receivedDeliveryData = receivedRepository.receivedDeliveryData
    val receivedSeeAllData = receivedRepository.receivedSeeAllData
    val receivedNextStoredData = receivedRepository.receivedNextData
    val receivedUserProfile = receivedRepository.receivedProfileData
    val receivedCheckOutProfile = receivedRepository.receivedCheckOutData

    fun getSlidingImage(firebaseTitle : String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            receivedRepository.getSlidingImage(firebaseTitle)
        }
    }
    fun getData(title : String?)
    {
        viewModelScope.launch(Dispatchers.IO) {
            receivedRepository.getData(title)
        }
    }
    fun getPlumberData(title : String?)
    {
        viewModelScope.launch(Dispatchers.IO) {
           receivedRepository.getPlumberData(title)
        }
    }
    fun getCleaningData(title: String?)
    {
        viewModelScope.launch(Dispatchers.IO) {
            receivedRepository.getCleaningData(title)
        }
    }
    fun getSalonData(title: String?)
    {
        viewModelScope.launch(Dispatchers.IO) {
            receivedRepository.getSalonData(title)
        }
    }
    fun getDeliveryData(title: String?)
    {
        viewModelScope.launch(Dispatchers.IO) {
            receivedRepository.getDeliveryData(title)
        }
    }
    fun getSeeAllData(title: String?)
    {
        viewModelScope.launch(Dispatchers.IO) {
            receivedRepository.getSeeAllData(title)
        }
    }
    fun getNextData(title : String?)
    {
        viewModelScope.launch(Dispatchers.IO) {
            receivedRepository.getNextData(title)
        }
    }
    fun getProfileData(title : String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            receivedRepository.getUserProfile(title)
        }
    }
    fun getCheckOutData(title : String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            receivedRepository.getCheckOutProfile(title)
        }
    }

}