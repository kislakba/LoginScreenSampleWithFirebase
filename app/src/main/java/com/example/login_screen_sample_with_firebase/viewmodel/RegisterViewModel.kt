package com.multilanguagechat.app.viewmodel

import android.app.AlertDialog
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.login_screen_sample_with_firebase.R
import com.example.login_screen_sample_with_firebase.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel : ViewModel() {
    private lateinit var dialog : AlertDialog

    fun register(
        email: String,
        password: String,
        username : String,
        auth: FirebaseAuth,
        nav : NavController,
        view: View?
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user!!.sendEmailVerification()
                        .addOnCompleteListener { taskVer ->
                            if (taskVer.isSuccessful) {
                                Snackbar.make(view!!, R.string.verify_Sent,Snackbar.LENGTH_LONG).show()
                                saveToDatabase(auth.uid,username)
                                auth.signOut()
                                nav.navigate(R.id.action_registerFragment_to_loginFragment)
                            }
                        }
                } else {
                    closeLoadingDialog()
                    Snackbar.make(view!!, R.string.already_member,Snackbar.LENGTH_LONG).show()
                }
            }
    }
    private fun saveToDatabase(uid : String?, username : String){
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")
        val user = uid?.let { User(it,username,false) }
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity","UserSaved To Database")
                closeLoadingDialog()
            }
            .addOnFailureListener {
                Log.d("err", "${user?.name} ${user?.uid}")
                closeLoadingDialog()
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