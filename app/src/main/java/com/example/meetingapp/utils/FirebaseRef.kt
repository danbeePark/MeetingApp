package com.example.meetingapp.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRef {
    companion object{
        val database = Firebase.database
        val userInfoRef = database.getReference("userInfoRef")
        val userLikeRef = database.getReference("userLike")


    }
}