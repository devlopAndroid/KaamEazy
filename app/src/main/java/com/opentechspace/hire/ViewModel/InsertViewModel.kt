package com.opentechspace.hire.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opentechspace.hire.Repositorry.InsertRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InsertViewModel(private val insertRepository: InsertRepository) : ViewModel() {

    val insertProfileLiveData = insertRepository.insertProfileLiveData
    val insertDoneLiveData = insertRepository.insertDoneLiveData
    val insertUpdatedProfileLiveData = insertRepository.insertUpdatedProfileLiveData
    val insertCheckoutProfileLiveData = insertRepository.insertCheckoutProfileLiveData
    fun insertProfileData(imageUri : String,name : String,age : String,email :String,mobile : String,city : String,houseNo: String,address : String,landmark : String,firebaseTitle : String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            insertRepository.insertProfileData(imageUri, name, age, email, mobile, city, houseNo, address, landmark,firebaseTitle)
        }
    }
    fun insertUpdatedProfileData(
        imageUri: String,
        name: String,
        age: String,
        email: String,
        mobile: String,
        city: String,
        houseNo: String,
        address: String,
        landmark: String,
        firebaseTitle: String,
        firebaseId: String?,
        checkValue: Int
    )
    {
        viewModelScope.launch(Dispatchers.IO) {
            insertRepository.insertUpdatedProfileData(imageUri, name, age, email, mobile, city, houseNo, address, landmark,firebaseTitle,firebaseId,checkValue)
        }
    }
    fun insertCheckoutProfileData(imageUri : String,name : String,price :String,firebaseTitle : String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            insertRepository.insertCheckOutData(imageUri,name,price, firebaseTitle)
        }
    }
    fun insertDoneData(Id : String, firebaseTitle: String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            insertRepository.insertDone(Id,firebaseTitle)
        }
    }
}