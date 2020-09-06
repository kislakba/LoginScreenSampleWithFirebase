package com.multilanguagechat.app.viewmodel

import android.app.AlertDialog
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.login_screen_sample_with_firebase.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class LoginViewModel : ViewModel() {
    private lateinit var dialog : AlertDialog
    fun loginWithMail(email : String, password : String, auth: FirebaseAuth, nav : NavController, view : View?) {
        if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                            if (user?.isEmailVerified!!) {
                                makeUserValid(user)
                                closeLoadingDialog()
                                nav.navigate(R.id.action_loginFragment_to_allUsersFragment)
                            }else{
                                closeLoadingDialog()
                                Snackbar.make(view!!,R.string.verify_your_acc, Snackbar.LENGTH_LONG)
                                    .show()
                            }
                        }else{
                            closeLoadingDialog()
                            Snackbar.make(view!!,R.string.login_failed, Snackbar.LENGTH_LONG)
                                .show()
                        }
                    }
        }else{
            closeLoadingDialog()
            Snackbar.make(view!!,R.string.fill_fields, Snackbar.LENGTH_LONG)
                .show()
        }
    }

    fun saveToDatabase(uid : String?, username : String){
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")
        val user = uid?.let { User(it,username,"https://cdn.pixabay.com/photo/2018/08/20/22/37/dog-3620181_960_720.jpg",false) }
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity","UserSaved To Database")
            }
            .addOnFailureListener {
                Log.d("err", "olmadi ${user?.name} ${user?.uid}")
            }


    }
    fun makeUserValid(user : FirebaseUser){
        val refValid = FirebaseDatabase.getInstance().getReference("users/${user.uid}/valid")
        refValid.setValue(true)
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