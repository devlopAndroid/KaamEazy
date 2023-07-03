package com.opentechspace.hire.Repositorry

import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class InsertRepository(private val storage : FirebaseStorage, private val firestore: FirebaseFirestore) {

    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    val insertProfileLiveData : MutableLiveData<String> = MutableLiveData<String>()
    val insertDoneLiveData : MutableLiveData<String> = MutableLiveData<String>()
    val insertUpdatedProfileLiveData : MutableLiveData<String> = MutableLiveData<String>()
    val insertCheckoutProfileLiveData : MutableLiveData<String> = MutableLiveData<String>()
//    private val  firebaseId = firestore.collection("UsersData").document().id
//    firebaseId : String? = null
    suspend fun insertProfileData(imageUri : String,name : String,age : String,email :String,mobile : String,city : String,houseNo: String,address : String,landmark : String,firebaseTitle : String) {
            val storageReference = storage.reference.child(name)
            val firebaseId = firestore.collection(firebaseTitle).document().id
            val imageName = storageReference.child(System.currentTimeMillis().toString())
            imageName.putFile(imageUri.toUri())
                .addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        imageName.downloadUrl.addOnSuccessListener { downloadUrl ->
                            val insertData = HashMap<String,Any>()
                            insertData["Id"] = firebaseId
                            insertData["ImageUrl"] = downloadUrl.toString()
                            insertData["Name"] = name
                            insertData["Age"] = age
                            insertData["Email"] = email
                            insertData["Mobile"] = mobile
                            insertData["City"] = city
                            insertData["HouseNo"] = houseNo
                            insertData["Address"] = address
                            insertData["LandMark"] = landmark

                            firestore.collection(firebaseTitle).document(firebaseAuth.currentUser!!.uid).collection("UsersData").document(firebaseId).set(insertData)
                                .addOnCompleteListener {
                                    insertProfileLiveData.postValue(it.toString())
                                }
                        }
                    }
                }
    }
    suspend fun insertUpdatedProfileData(
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
    ) {

        if(checkValue == 1)
        {
            val insertData = HashMap<String, Any>()
            insertData["Id"] = firebaseId!!
            insertData["ImageUrl"] = imageUri
            insertData["Name"] = name
            insertData["Age"] = age
            insertData["Email"] = email
            insertData["Mobile"] = mobile
            insertData["City"] = city
            insertData["HouseNo"] = houseNo
            insertData["Address"] = address
            insertData["LandMark"] = landmark

            firestore.collection(firebaseTitle).document(firebaseAuth.currentUser!!.uid)
                .collection("UsersData").document(firebaseId!!).set(insertData)
                .addOnCompleteListener {
                    insertUpdatedProfileLiveData.postValue(it.toString())
                }
        }
        else
        {
            val storageReference = storage.reference.child(name)
//        val firebaseId = firestore.collection(firebaseTitle).document().id
            val imageName = storageReference.child(System.currentTimeMillis().toString())
            imageName.putFile(imageUri.toUri())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        imageName.downloadUrl.addOnSuccessListener { downloadUrl ->
                            val insertData = HashMap<String, Any>()
                            insertData["Id"] = firebaseId!!
                            insertData["ImageUrl"] = downloadUrl.toString()
                            insertData["Name"] = name
                            insertData["Age"] = age
                            insertData["Email"] = email
                            insertData["Mobile"] = mobile
                            insertData["City"] = city
                            insertData["HouseNo"] = houseNo
                            insertData["Address"] = address
                            insertData["LandMark"] = landmark

                            firestore.collection(firebaseTitle).document(firebaseAuth.currentUser!!.uid)
                                .collection("UsersData").document(firebaseId).set(insertData)
                                .addOnCompleteListener {
                                    insertUpdatedProfileLiveData.postValue(it.toString())
                                }
                        }
                    }
                }
        }
    }
    suspend fun insertCheckOutData(imageUri: String, name: String,price: String, firebaseTitle: String) {
            val firebaseId = firestore.collection(firebaseTitle).document().id
            val insertData = HashMap<String, Any>()
            insertData["Id"] = firebaseId
            insertData["Name"] = name
            insertData["ImageUrl"] = imageUri
            insertData["Price"] = price
            firestore.collection(firebaseTitle).document(firebaseAuth.currentUser!!.uid)
                .collection("CheckOut").document(firebaseId).set(insertData)
                .addOnCompleteListener {
                    insertCheckoutProfileLiveData.postValue(it.toString())
                }
        }

    suspend fun insertDone(ID : String,firebaseTitle: String)
    {
        val firebaseId = firestore.collection(firebaseTitle).document().id
        val insertData = HashMap<String, Any>()
        insertData["Id"] = firebaseId
        insertData["UserId"] = ID
        firestore.collection(firebaseTitle).document(firebaseId).set(insertData)
            .addOnCompleteListener {
                insertDoneLiveData.postValue(it.toString())
            }
    }

}