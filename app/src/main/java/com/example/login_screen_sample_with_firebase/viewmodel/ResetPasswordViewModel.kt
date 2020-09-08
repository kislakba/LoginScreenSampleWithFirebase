package com.multilanguagechat.app.viewmodel

import android.app.AlertDialog
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.login_screen_sample_with_firebase.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordViewModel : ViewModel() {
    private lateinit var dialog : AlertDialog
    fun sendResetEmail(email: String, nav : NavController,
                       auth: FirebaseAuth, view: View?) {
        if (!TextUtils.isEmpty(email)) {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Snackbar.make(view!!, R.string.reset_pass, Snackbar.LENGTH_LONG)
                            .show()
                        closeLoadingDialog()
                        nav.navigate(R.id.action_resetPasswordFragment_to_loginFragment)
                    } else {
                        closeLoadingDialog()
                        Snackbar.make(view!!, R.string.enter_valid_mail, Snackbar.LENGTH_LONG)
                            .show()
                    }
                }
        }else{
            closeLoadingDialog()
            Snackbar.make(view!!,R.string.fill_fields,Snackbar.LENGTH_SHORT).show()
        }
    }
    fun openLoadingDialog(fragmentActivity: FragmentActivity){
        val progress = AlertDialog.Builder(fragmentActivity)
        val dialogView = fragmentActivity.layoutInflater.inflate(R.layout.dialog_progress,null)
        progress.setView(dialogView)
        progress.setCancelable(false)
        dialog = progress.create()
        dialog.show()
    }
    fun closeLoadingDialog(){
        dialog.dismiss()
    }
}