package com.lemontimee.hedefprogramdenemev1.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lemontimee.hedefprogramdenemev1.R
import com.lemontimee.hedefprogramdenemev1.util.hideSystemUI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //throw RuntimeException("Test Crash") //for testing crashlytics

        auth = Firebase.auth

        buttonLogin.setOnClickListener {
            myIntent(1)
        }
        buttonRegister.setOnClickListener {
            myIntent(2)
        }

        //system UI hiding when activity created.
        hideSystemUI()

    }

    //if user press home button or quit app, onCreate won't get called (onResume also useable).
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus){
            hideSystemUI()
        }
    }

    private fun myIntent(value: Int){
        val intent = Intent(this,ActivityLogin::class.java)
        intent.putExtra("button",value)
        startActivity(intent)
       // throw RuntimeException("Test Crash") //for testing crashlytics
    }


}