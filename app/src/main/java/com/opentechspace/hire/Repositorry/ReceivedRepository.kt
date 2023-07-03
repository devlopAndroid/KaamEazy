package com.opentechspace.hire.Repositorry

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.opentechspace.hire.Model.*

class ReceivedRepository(private val firestore: FirebaseFirestore) {


    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val mutableSlidingImage : MutableLiveData<List<SlidingImageModel>> = MutableLiveData()
    val slidingImageLiveData : LiveData<List<SlidingImageModel>> = mutableSlidingImage
    private val receivedLiveData : MutableLiveData<List<ItemModel>> = MutableLiveData()
    var receivedData : LiveData<List<ItemModel>> = receivedLiveData
    private val receivedPlumberLiveData : MutableLiveData<List<ItemPlumberModel>> = MutableLiveData()
    var receivedPlumberData : LiveData<List<ItemPlumberModel>> = receivedPlumberLiveData
    private val receivedCleaningLiveData : MutableLiveData<List<ItemCleaningModel>> = MutableLiveData()
    var receivedCleaningData : LiveData<List<ItemCleaningModel>> = receivedCleaningLiveData
    private val receivedSalonLiveData : MutableLiveData<List<ItemSalonModel>> = MutableLiveData()
    var receivedSalonData : LiveData<List<ItemSalonModel>> = receivedSalonLiveData
    private val receivedDeliveryLiveData : MutableLiveData<List<ItemDeliveryModel>> = MutableLiveData()
    var receivedDeliveryData : LiveData<List<ItemDeliveryModel>> = receivedDeliveryLiveData
    private val receivedSeeAllLiveData : MutableLiveData<List<SeeAllItemModel>> = MutableLiveData()
    var receivedSeeAllData : LiveData<List<SeeAllItemModel>> = receivedSeeAllLiveData
    private val receivedNextLiveData : MutableLiveData<List<ItemNextModel>> = MutableLiveData()
    var receivedNextData : LiveData<List<ItemNextModel>> = receivedNextLiveData
    private val receivedProfileLiveData : MutableLiveData<List<ProfileModel>> = MutableLiveData()
    var receivedProfileData : LiveData<List<ProfileModel>> = receivedProfileLiveData
    private val receivedCheckOutLiveData : MutableLiveData<List<CheckOutItemModel>> = MutableLiveData()
    var receivedCheckOutData : LiveData<List<CheckOutItemModel>> = receivedCheckOutLiveData

    suspend fun getSlidingImage(firebaseTitle : String)
    {
        firestore.collection(firebaseTitle)
            .addSnapshotListener { value, error ->
                val result = value?.toObjects(SlidingImageModel::class.java)
                if(result != null)
                {
                    mutableSlidingImage.postValue(result!!)
                }
                else
                {
                    Log.e("Error", error?.message.toString())
                }
            }
    }

    suspend fun getData(title : String?){

        firestore.collection(title!!).
        addSnapshotListener{ result , error->
            val data = result?.toObjects(ItemModel::class.java)
            if(data != null)
            {
                receivedLiveData.postValue(data!!)
            }
            else
            {
                Log.e("Error", error!!.message.toString())
            }
        }
    }
    suspend fun getPlumberData(title : String?){

        firestore.collection(title!!).
        addSnapshotListener{ result , error->
            val data = result?.toObjects(ItemPlumberModel::class.java)
            if(data != null)
            {
                receivedPlumberLiveData.postValue(data!!)
            }
            else
            {
                Log.e("Error", error!!.message.toString())
            }
        }
    }
    suspend fun getCleaningData(title : String?){

        firestore.collection(title!!).
        addSnapshotListener{ result , error->
            val data = result?.toObjects(ItemCleaningModel::class.java)
            if(data != null)
            {
                receivedCleaningLiveData.postValue(data!!)
            }
            else
            {
                Log.e("Error", error!!.message.toString())
            }
        }
    }
    suspend fun getSalonData(title : String?){

        firestore.collection(title!!).
        addSnapshotListener{ result , error->
            val data = result?.toObjects(ItemSalonModel::class.java)
            if(data != null)
            {
                receivedSalonLiveData.postValue(data!!)
            }
            else
            {
                Log.e("Error", error!!.message.toString())
            }
        }
    }

    suspend fun getDeliveryData(title : String?){

        firestore.collection(title!!).
        addSnapshotListener{ result , error->
            val data = result?.toObjects(ItemDeliveryModel::class.java)
            if(data != null)
            {
                receivedDeliveryLiveData.postValue(data!!)
            }
            else
            {
                Log.e("Error", error!!.message.toString())
            }
        }
    }
    suspend fun getSeeAllData(title : String?){

        firestore.collection(title!!).
        addSnapshotListener{ result , error->
            val data = result?.toObjects(SeeAllItemModel::class.java)
            if(data != null)
            {
                receivedSeeAllLiveData.postValue(data!!)
            }
            else
            {
                Log.e("Error", error!!.message.toString())
            }
        }
    }
    suspend fun getNextData(title : String?){

        firestore.collection(title!!).
        addSnapshotListener{ result , error->
            val data = result?.toObjects(ItemNextModel::class.java)
            if(data != null)
            {
                receivedNextLiveData.postValue(data!!)
            }
            else
            {
                Log.e("Error", error!!.message.toString())
            }
        }
    }
    suspend fun getUserProfile(firebaseTitle : String)
    {
        firestore.collection(firebaseTitle).document(firebaseAuth.currentUser!!.uid).collection("UsersData")
            .addSnapshotListener { value, error ->
                val data = value?.toObjects(ProfileModel::class.java)
                if(data != null)
                {
                    receivedProfileLiveData.postValue(data!!)
                }
                else
                {
                    Log.e("ProfileError",error!!.message.toString())
                }

            }
    }
    suspend fun getCheckOutProfile(firebaseTitle : String)
    {
        firestore.collection(firebaseTitle).document(firebaseAuth.currentUser!!.uid).collection("CheckOut")
            .addSnapshotListener { value, error ->
                val data = value?.toObjects(CheckOutItemModel::class.java)
                if(data != null)
                {
                    receivedCheckOutLiveData.postValue(data!!)
                }
                else
                {
                    Log.e("ProfileError",error!!.message.toString())
                }

            }
    }
}