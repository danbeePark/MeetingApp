package com.example.meetingapp.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.meetingapp.R
import com.example.meetingapp.auth.IntroActivity
import com.example.meetingapp.utils.FirebaseAuthUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingActivity : AppCompatActivity() {
    private var TAG = "SettingActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)



        val myBtn = findViewById<Button>(R.id.myPageBtn)
        myBtn.setOnClickListener {

            val intent = Intent(this, MypageAcitivity::class.java)
            startActivity(intent)
        }

        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        logoutBtn.setOnClickListener {

            val auth = Firebase.auth
            auth.signOut()

            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }

    }

}