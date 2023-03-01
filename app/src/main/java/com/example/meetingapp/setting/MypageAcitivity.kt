package com.example.meetingapp.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.meetingapp.R
import com.example.meetingapp.auth.UserDataModel
import com.example.meetingapp.utils.FirebaseAuthUtils
import com.example.meetingapp.utils.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MypageAcitivity : AppCompatActivity() {
    private val TAG = "MypageAcitivity"
    private var uid = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_acitivity)

        getMyData()
    }

    private fun getMyData() {
        // Read from the database

        uid = FirebaseAuthUtils.getUid()
        Log.d(TAG, "uid=$uid")

        val myUid = findViewById<TextView>(R.id.uidArea)
        val myNickname = findViewById<TextView>(R.id.nicknameArea)
        val myAge = findViewById<TextView>(R.id.ageArea)
        val myCity = findViewById<TextView>(R.id.cityArea)
        val myGender = findViewById<TextView>(R.id.genderArea)

        val myImage = findViewById<ImageView>(R.id.myImage)


        FirebaseRef.userInfoRef.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val data = dataSnapshot.getValue(UserDataModel::class.java)
                myUid.text = data?.uid
                myNickname.text = data?.nickname
                myAge.text = data?.age
                myCity.text = data?.city
                myGender.text = data?.gender
                val storageReference = Firebase.storage.reference
                val pathReference = storageReference.child("$uid.jpg")

                pathReference.downloadUrl.addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        Glide.with(baseContext)
                            .load(task.result)
                            .into(myImage)

                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}