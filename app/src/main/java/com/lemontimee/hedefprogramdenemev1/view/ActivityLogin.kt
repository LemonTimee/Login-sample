package com.lemontimee.hedefprogramdenemev1.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import com.lemontimee.hedefprogramdenemev1.R
import com.lemontimee.hedefprogramdenemev1.util.hideKeyboard
import com.lemontimee.hedefprogramdenemev1.util.hideSystemUI

class ActivityLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val getExtra = intent.getIntExtra("button",1)
        if (getExtra == 1){
            login()
        }
        else if (getExtra == 2){
            register()
        }

        //system UI hiding when activity created.
        hideSystemUI()
    }

    //hide keyboard when focus another view
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard()
        return super.dispatchTouchEvent(ev)
    }

    private fun login(){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragmentLogin = FragmentLogin()
        fragmentTransaction.replace(R.id.frameLayout,fragmentLogin).commit()
    }

    private fun register(){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragmentRegister = FragmentRegister()
        fragmentTransaction.replace(R.id.frameLayout,fragmentRegister).commit()
    }

    //if user press home button or quit app, onCreate won't get called (onResume also useable).
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus){
            hideSystemUI()
        }
    }

}