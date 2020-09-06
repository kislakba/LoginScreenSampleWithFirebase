package com.multilanguagechat.app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.multilanguagechat.app.R
import com.multilanguagechat.app.viewmodel.ResetPasswordViewModel
import kotlinx.android.synthetic.main.fragment_forgotpass.*
class ResetPasswordFragment : Fragment() {
    private val viewModel = ResetPasswordViewModel()
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgotpass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val nav = Navigation.findNavController(requireView())
        send_email_btn.setOnClickListener {
            viewModel.openLoadingDialog(requireActivity())
            viewModel.sendResetEmail(IdTextField.text.toString(),nav,auth,view)
        }
    }
}