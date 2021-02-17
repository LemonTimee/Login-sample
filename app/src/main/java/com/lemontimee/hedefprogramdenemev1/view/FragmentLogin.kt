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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lemontimee.hedefprogramdenemev1.R
import com.lemontimee.hedefprogramdenemev1.databinding.FragmentLoginBinding
import com.lemontimee.hedefprogramdenemev1.viewmodel.LoginRegisterViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class FragmentLogin : Fragment() {

    companion object {
        const val GOOGLE_SIGN_IN = 1903
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: LoginRegisterViewModel
    private lateinit var dataBinding: FragmentLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        googleSignInClient = GoogleSignIn.getClient(requireContext(),getGSO())
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View {

        dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkCurrentUser()

        viewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel::class.java)
        dataBinding.viewmodel = viewModel
        dataBinding.lifecycleOwner = this

        observeLiveData()

        imageButtonBackLogin.setOnClickListener {
            activity?.let { act->
                act.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
                act.finish()
            }
        }

        buttonLoginResetPassword.setOnClickListener {
            goTo()
        }

        buttonLogin.setOnClickListener {
            if (viewModel.emailErrorMLD.value == null
                    && viewModel.passwordErrorMLD.value == null
                    && viewModel.emailMLD.value != null
                    && viewModel.passwordMLD.value != null){

                loginWithoutGoogle()
            }
            else{
                Toast.makeText(context,"Please enter valid value",Toast.LENGTH_LONG).show()
            }
        }

        buttonLoginGoogle.setOnClickListener {
            loginWithGoogle()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Check condition
        if (requestCode == GOOGLE_SIGN_IN){
            //When request code is equal to 100
            //Initialize task
            val task: Task<GoogleSignInAccount> = GoogleSignIn
                    .getSignedInAccountFromIntent(data)
            //Check condition
            if (task.isSuccessful){
                //When google sign in successfull
                //Initialize string
                try {
                    //Initialize sign in account
                    val googleSignInAccount = task
                            .getResult(ApiException::class.java)
                    //Check condition
                    if (googleSignInAccount != null){
                        if (googleSignInAccount.idToken != null){
                            firebaseAuthWithGoogle(googleSignInAccount.idToken!!)
                        }
                    }
                }catch (e: ApiException){
                    e.printStackTrace()
                }
            }
        }
    }

    //observing all edittext's live data
    private fun observeLiveData(){
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
    }

    //using in login button
    private fun loginWithoutGoogle(){

        val email = viewModel.emailMLD.value!!
        val password = viewModel.passwordMLD.value!!

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful){
                mainLogin()
            }
        }.addOnFailureListener {
            Toast.makeText(context,it.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }

    private fun mainLogin(){
        activity?.let { act->
            val intent = Intent(activity,ActivityEmpty::class.java)
            startActivity(intent)
            act.finish()
        }
    }

    //login with google
    private fun getGSO(): GoogleSignInOptions{
        return GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
    }

    //login with google
    private fun loginWithGoogle(){
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, GOOGLE_SIGN_IN)
    }

    //login with google
    private fun firebaseAuthWithGoogle(idToken: String){
        //When sign in account is not equal to null
        //Initialize auth credential
        val authCredential = GoogleAuthProvider
                .getCredential(idToken,null)
        //Check credential
        auth.signInWithCredential(authCredential)
                .addOnCompleteListener { task->
                    //Check condition
                    if (task.isSuccessful){
                        //When task is successfull
                        //Redirect to profile activity
                        val intent = Intent(activity,ActivityEmpty::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        Toast.makeText(context,"Authentication Successful",Toast.LENGTH_LONG).show()
                    }
                    else{
                        //When task is unsucessfull
                        Toast.makeText(context,"Authentication Failed : " + task.exception,Toast.LENGTH_LONG).show()
                    }
                }
    }

    private fun checkCurrentUser(){
        val currentUser = auth.currentUser
        if (currentUser != null){
            mainLogin()
        }
    }

    private fun goTo(){
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragmentForgotPassword = FragmentForgotPassword()
        fragmentTransaction.replace(R.id.frameLayout,fragmentForgotPassword).commit()
    }
}