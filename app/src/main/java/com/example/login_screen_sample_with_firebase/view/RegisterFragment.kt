package com.multilanguagechat.app.view

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.multilanguagechat.app.R
import com.multilanguagechat.app.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.fragment_registerr.*

class RegisterFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private val viewModel = RegisterViewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_registerr, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val nav = Navigation.findNavController(requireView())
        registerButton.setOnClickListener {
            val emailSTR = IdTextField.text.toString()
            val passStr = passwordTextField.text.toString()
            var username = usernameTextField.text.toString()
            viewModel.openLoadingDialog(requireActivity())
            if (!TextUtils.isEmpty(emailSTR) && Patterns.EMAIL_ADDRESS.matcher(emailSTR).matches()
                && passStr.length > 7 && username.trim().length > 4)
            {
                viewModel.register(emailSTR, passStr,username, auth, nav, view)
            } else if (passStr.length < 8) {
                    viewModel.closeLoadingDialog()
                    Snackbar.make(it, R.string.enter_valid_password,Snackbar.LENGTH_LONG).show()
            } else {
                viewModel.closeLoadingDialog()
                Snackbar.make(it,R.string.enter_valid_mail,Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}