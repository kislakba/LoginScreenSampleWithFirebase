package com.example.login_screen_sample_with_firebase.model

class User(val uid : String,val name : String, val photo_url : String, val valid : Boolean) {
    constructor() : this("","","",false)

}