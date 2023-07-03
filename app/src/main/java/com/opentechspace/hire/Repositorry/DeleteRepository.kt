package com.opentechspace.hire.Repositorry

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.opentechspace.hire.Model.CheckOutItemModel

class DeleteRepository(private val firebaseFirestore: FirebaseFirestore, private val storage: FirebaseStorage) {

    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    val deleteItemLiveData : MutableLiveData<String> = MutableLiveData()

    suspend fun deleteCheckOut(checkOutItem : CheckOutItemModel)
    {
        checkOutItem.Id.let {
            firebaseFirestore.collection("UsersProfile").document(firebaseAuth.currentUser!!.uid)
                .collection("CheckOut").document(it!!).delete()
                .addOnCompleteListener {
                    deleteItemLiveData.postValue(it.toString())
                }
        }
    }

}