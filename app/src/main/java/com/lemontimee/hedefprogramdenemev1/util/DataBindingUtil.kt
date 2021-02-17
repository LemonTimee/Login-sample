package com.lemontimee.hedefprogramdenemev1.util

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("app:errorUsername")
fun errorUsername(view: TextInputLayout, errorMessage: String?){
    view.isErrorEnabled = true
    view.error = errorMessage
}

@BindingAdapter("errorEmail")
fun errorEmail(view: TextInputLayout, errorMessage: String?){
    view.isErrorEnabled = true
    view.error = errorMessage
}

@BindingAdapter("errorPassword")
fun errorPassword(view: TextInputLayout, errorMessage: String?){
    view.isErrorEnabled = true
    view.error = errorMessage
}

@BindingAdapter("errorPassCon")
fun errorPasswordConfirm(view: TextInputLayout, errorMessage: String?){
    view.isErrorEnabled = true
    view.error = errorMessage
}