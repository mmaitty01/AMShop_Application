package org.swu.aeymai.amshop.Repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository {
    private val firestoreDB: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun getSavedSearch(): CollectionReference {
        var collectionReference = firestoreDB.collection("amShopDB")
        return collectionReference
    }
}