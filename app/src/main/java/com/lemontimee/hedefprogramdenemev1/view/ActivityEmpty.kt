package com.lemontimee.hedefprogramdenemev1.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lemontimee.hedefprogramdenemev1.R
import com.lemontimee.hedefprogramdenemev1.databinding.ActivityEmptyBinding
import com.lemontimee.hedefprogramdenemev1.viewmodel.ActivityEmptyViewModel
import kotlinx.android.synthetic.main.activity_empty.*

class ActivityEmpty : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSingInClient: GoogleSignInClient
    private lateinit var database: FirebaseFirestore
    private lateinit var dataBinding: ActivityEmptyBinding
    private lateinit var viewModel: ActivityEmptyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView(this,R.layout.activity_empty)
        viewModel = ViewModelProviders.of(this).get(ActivityEmptyViewModel::class.java)
        dataBinding.viewmodel = viewModel
        dataBinding.lifecycleOwner = this
        auth = FirebaseAuth.getInstance()
        googleSingInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)
        database = Firebase.firestore

        checkCurrentUser()

        buttonLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout(){
        googleSingInClient.signOut().addOnCompleteListener {

            if (it.isSuccessful){
                //When task is successful
                //Sign out from firebase
                auth.signOut()
                Toast.makeText(applicationContext, "Logout Successful", Toast.LENGTH_SHORT).show()

                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun checkCurrentUser(){
        val currentUser = auth.currentUser

        if (currentUser != null){
            //When firebase user is not equal to null
            viewModel.readUser(database,currentUser.email!!)
        }
    }


}