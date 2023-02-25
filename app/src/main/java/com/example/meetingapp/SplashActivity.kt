package com.example.meetingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.meetingapp.auth.IntroActivity
import com.example.meetingapp.utils.FirebaseAuthUtils
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private val TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        val uid = auth.currentUser?.uid.toString()
//        Log.d(TAG, auth.currentUser?.uid.toString())

        val uid = FirebaseAuthUtils.getUid()
        if(uid=="null") {
            Handler().postDelayed(
                {
                    var intent = Intent(this, IntroActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    finish()
                }, 3000
            )
        }else{
            Handler().postDelayed(
                {
                    var intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    finish()
                }, 3000
            )
        }
    }
}