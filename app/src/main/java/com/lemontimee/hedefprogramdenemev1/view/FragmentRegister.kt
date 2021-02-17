package com.lemontimee.hedefprogramdenemev1.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lemontimee.hedefprogramdenemev1.R
import com.lemontimee.hedefprogramdenemev1.databinding.FragmentRegisterBinding
import com.lemontimee.hedefprogramdenemev1.service.UserAPI
import com.lemontimee.hedefprogramdenemev1.viewmodel.LoginRegisterViewModel
import kotlinx.android.synthetic.main.fragment_register.*

class FragmentRegister : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dataBinding: FragmentRegisterBinding
    private lateinit var viewModel: LoginRegisterViewModel
    private lateinit var database: FirebaseFirestore
    private val userAPI = UserAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        database = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_register,container,false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel::class.java)
        dataBinding.viewmodel = viewModel
        dataBinding.lifecycleOwner = this

        observeLiveData()

        imageButtonBackRegister.setOnClickListener {
            activity?.let { act->
                act.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
                act.finish()
            }
        }

        //checking all edittext and live data is not empty/null
        //needing null check because if we dont get error on edittext, put null value in the live data
        buttonRegisterRegister.setOnClickListener {
            if (viewModel.passwordConfirmErrorMLD.value == null
                    && viewModel.passwordErrorMLD.value == null
                    && viewModel.emailErrorMLD.value == null
                    && viewModel.usernameErrorMLD.value == null
                    && viewModel.passwordConfirmMLD.value != null
                    && viewModel.passwordMLD.value != null
                    && viewModel.emailMLD.value != null
                    && viewModel.usernameMLD.value != null) {

                createAccount()

            } else {
                Toast.makeText(context, "Please enter valid value", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //observing all edittext's live data
    private fun observeLiveData(){
        viewModel.usernameMLD.observe(viewLifecycleOwner, {
            if (viewModel.isUsernameValid(it.toString())){
                viewModel.returnHandler(1,viewModel.usernameErrorMLD)
            }
            else{
                viewModel.returnHandler(2,viewModel.usernameErrorMLD)
            }
        })

        viewModel.emailMLD.observe(viewLifecycleOwner, {
            if (viewModel.isEmailValid(it.toString())){
                viewModel.returnHandler(3,viewModel.emailErrorMLD)
            }
            else{
                viewModel.returnHandler(4,viewModel.emailErrorMLD)
            }
        })

        viewModel.passwordMLD.observe(viewLifecycleOwner, {
            if (viewModel.isPasswordValid(it.toString())){
                viewModel.returnHandler(5,viewModel.passwordErrorMLD)
            }
            else{
                viewModel.returnHandler(6,viewModel.passwordErrorMLD)
            }
        })

        viewModel.passwordConfirmMLD.observe(viewLifecycleOwner, {
            if (viewModel.isPasswordConfirmOk()){
                viewModel.returnHandler(7,viewModel.passwordConfirmErrorMLD)
            }
            else{
                viewModel.returnHandler(8,viewModel.passwordConfirmErrorMLD)
            }
        })
    }

    //creating account new account and intent
    private fun createAccount(){

        val email = viewModel.emailMLD.value!!
        val password = viewModel.passwordMLD.value!!
        val username = viewModel.usernameMLD.value!!
        val date = Timestamp.now()

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful){

                userAPI.writeUser(database,username,email,date)
                activity?.let { act->
                    val intent = Intent(activity,ActivityEmpty::class.java)
                    startActivity(intent)
                    act.finish()
                }

                Toast.makeText(context,"Account Created",Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(context,it.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }

}

