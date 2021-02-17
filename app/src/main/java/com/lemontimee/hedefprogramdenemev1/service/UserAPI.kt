package com.lemontimee.hedefprogramdenemev1.service

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class UserAPI {

    fun writeUser(database: FirebaseFirestore, username: String, email: String, date: Timestamp){

        val userMap = hashMapOf<String, Any>()

        userMap["username"] = username
        userMap["email"] = email
        userMap["date"] = date

        database.collection("user").add(userMap).addOnCompleteListener {
            if (it.isSuccessful){
                println("firestore kaydı atıldı")
            }
        }.addOnFailureListener {
            println("firestore kaydı hata aldı")
            println(it.localizedMessage)
        }
    }
}
