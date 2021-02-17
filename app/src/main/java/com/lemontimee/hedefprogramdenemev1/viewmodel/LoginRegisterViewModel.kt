package com.lemontimee.hedefprogramdenemev1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.regex.Matcher
import java.util.regex.Pattern

class LoginRegisterViewModel: ViewModel() {

    val usernameMLD = MutableLiveData<String>()
    val emailMLD = MutableLiveData<String>()
    val passwordMLD = MutableLiveData<String>()
    val passwordConfirmMLD = MutableLiveData<String>()
    val usernameErrorMLD = MutableLiveData<String>()
    val emailErrorMLD = MutableLiveData<String>()
    val passwordErrorMLD = MutableLiveData<String>()
    val passwordConfirmErrorMLD = MutableLiveData<String>()

    fun isUsernameValid(username: String):Boolean{
        val expression = "^[a-zA-Z0-9_-]{5,15}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(username)
        return matcher.matches()
    }

    fun isEmailValid(email: String): Boolean{
        val expression = "^[\\w-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun isPasswordValid(password: String): Boolean{
        val expression = "^(?=.*[a-zA-Z])(?=\\S+\$).{7,}\$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun isPasswordConfirmOk():Boolean{
        if (passwordMLD.value.equals(passwordConfirmMLD.value)){
            return true
        }
        return false
    }

    //handling live data error
    fun returnHandler(number: Int, list:MutableLiveData<String>){
        when (number) {
            1 -> {
                list.value = null
            }
            2 -> {
                list.value = "min 5 character"
            }
            3 -> {
                list.value = null
            }
            4 -> {
                list.value = "email is not valid"
            }
            5 -> {
                list.value = null
            }
            6 -> {
                list.value = "min 7 character(min 1 letter)"
            }
            7 -> {
                list.value = null
            }
            else -> {
                list.value = "not same with password"
            }
        }
    }
}