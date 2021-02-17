package com.lemontimee.hedefprogramdenemev1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.lemontimee.hedefprogramdenemev1.model.User

class ActivityEmptyViewModel: ViewModel() {

    val usernameEmptyMLD = MutableLiveData<String>()

    fun readUser(database: FirebaseFirestore, email: String){

        database.collection("user").whereEqualTo("email",email).addSnapshotListener { snapshot, error ->
            if (error != null){
                println("firestoredan kayıt çekildi")
            }
            else{
                if (snapshot != null){
                    if (!snapshot.isEmpty){
                        val documents = snapshot.documents

                        for (d in documents){
                            val username = d.get("username") as String
                            val emails = d.get("email") as String
                            val date = d.get("date") as Timestamp

                            val user = User(username,emails,date)
                            println(user.userName)
                            usernameEmptyMLD.value = username
                            println("listenin içi = " + usernameEmptyMLD.value.toString())
                        }
                    }
                }
            }
        }
    }
}