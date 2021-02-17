package com.lemontimee.hedefprogramdenemev1.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lemontimee.hedefprogramdenemev1.R
import com.lemontimee.hedefprogramdenemev1.databinding.FragmentForgotPasswordBinding
import com.lemontimee.hedefprogramdenemev1.viewmodel.LoginRegisterViewModel
import kotlinx.android.synthetic.main.fragment_forgot_password.*

class FragmentForgotPassword : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dataBinding: FragmentForgotPasswordBinding
    private lateinit var viewModel: LoginRegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_forgot_password,container,false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel::class.java)
        dataBinding.viewmodel = viewModel
        dataBinding.lifecycleOwner = this

        observeLiveData()

        imageButtonBackForgot.setOnClickListener {
            activity?.let { act ->
                act.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
                act.finish()
            }
        }

        buttonForgotResetPassword.setOnClickListener {
            if (viewModel.emailErrorMLD.value == null
                    && viewModel.emailMLD.value != null){
                resetPassword()
            }
            else{
                Toast.makeText(context,"Please enter valid email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeLiveData(){
        viewModel.emailMLD.observe(viewLifecycleOwner, {
            if (viewModel.isEmailValid(it.toString())){
                viewModel.returnHandler(3,viewModel.emailErrorMLD)
            }
            else{
                viewModel.returnHandler(4,viewModel.emailErrorMLD)
            }
        })
    }

    private fun resetPassword(){

        val email = viewModel.emailMLD.value!!
        progressBarResetPassword.visibility = View.VISIBLE

        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful){
                progressBarResetPassword.visibility = View.GONE
                Toast.makeText(context,"Check your mailbox to reset your password!",Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(context,it.localizedMessage,Toast.LENGTH_LONG).show()
            progressBarResetPassword.visibility = View.GONE
        }
    }
}