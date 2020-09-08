package com.example.login_screen_sample_with_firebase.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.login_screen_sample_with_firebase.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.multilanguagechat.app.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val viewModel = LoginViewModel()
    private lateinit var nav : NavController
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {  //for google sign in function
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                viewModel.closeLoadingDialog()
                Snackbar.make(requireView(), R.string.login_failed, Snackbar.LENGTH_LONG)
                    .show()

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //.requestIdToken(getString(R.string.default_web_client_id)) this row will be activate when you add google-services.json file from firebase
            .requestEmail()
            .build()
        mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }!!

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        nav = Navigation.findNavController(requireView())
        if(auth.currentUser != null){
            //nav.navigate(R.id.action_loginFragment_to_allUsersFragment)  it will navigate your enter screen
        }
        loginButton.setOnClickListener {
            viewModel.openLoadingDialog(requireActivity())
            viewModel.loginWithMail(emailText.text.toString(), passwordText.text.toString(), auth, nav, view)
        }
        googleSignInButton.setOnClickListener {
            viewModel.openLoadingDialog(requireActivity())
            loginWithGoogle()
        }
        toregisterButton.setOnClickListener {
            nav.navigate(R.id.action_loginFragment_to_registerFragment)
        }
        forgetButton.setOnClickListener {
            nav.navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }
    }

    private fun loginWithGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    private fun firebaseAuthWithGoogle(idToken: String) { // with this func. you can easily sign in with google
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Snackbar.make(requireView(),R.string.login_success,Snackbar.LENGTH_LONG)
                        .show()
                    viewModel.saveToDatabase(auth.currentUser?.uid, auth.currentUser?.displayName!!)
                    viewModel.makeUserValid(auth.currentUser!!)
                    //nav.navigate(R.id.action_loginFragment_to_allUsersFragment)   --> you can navigate to your main page
                    viewModel.closeLoadingDialog()
                } else {
                    viewModel.closeLoadingDialog()
                    Snackbar.make(requireView(), R.string.login_failed, Snackbar.LENGTH_LONG)
                        .show()
                }
            }
    }
        companion object {

        private const val RC_SIGN_IN = 123
    }
}
