package com.example.meetingapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.meetingapp.MainActivity
import com.example.meetingapp.R
import com.example.meetingapp.utils.FirebaseRef
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class JoinActivity : AppCompatActivity() {
    private val TAG = "JoinActivity"
    private lateinit var auth: FirebaseAuth
    private var nickname =""
    private var city =""
    private var age = ""
    private var gender = ""
    private var uid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        val joinBtn = findViewById<Button>(R.id.joinBtn)
        joinBtn.setOnClickListener {
// Initialize Firebase Auth


            auth = Firebase.auth

            //닉네임 성별 지역 나이 uid
            val email = findViewById<TextInputEditText>(R.id.emailArea)
            val pwd = findViewById<TextInputEditText>(R.id.passwordArea)

            nickname = findViewById<TextInputEditText>(R.id.nicknameArea).text.toString()
            city = findViewById<TextInputEditText>(R.id.cityArea).text.toString()
            age = findViewById<TextInputEditText>(R.id.ageArea).text.toString()
            gender = findViewById<TextInputEditText>(R.id.genderArea).text.toString()

            auth.createUserWithEmailAndPassword(email.text.toString(), pwd.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        uid = user?.uid.toString()

                        val userModel =UserDataModel(uid, nickname, age, gender, city)
                        FirebaseRef.userInfoRef.child(uid).setValue(userModel)

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)

                    }
                }
        }
    }
}