package com.example.meetingapp.auth

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.meetingapp.MainActivity
import com.example.meetingapp.R
import com.example.meetingapp.utils.FirebaseRef
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class JoinActivity : AppCompatActivity() {
    private val TAG = "JoinActivity"
    private lateinit var auth: FirebaseAuth
    private var nickname =""
    private var city =""
    private var age = ""
    private var gender = ""
    private var uid = ""
    lateinit var profileImage :ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        profileImage =findViewById<ImageView>(R.id.imageArea)

        val getAction =
            registerForActivityResult(
                ActivityResultContracts.GetContent(),
                ActivityResultCallback {
                        uri ->
                    profileImage.setImageURI(uri)
                }
            )
        profileImage.setOnClickListener {

            Log.d(TAG, "Test")
            getAction.launch("image/*")
        }

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

                        uploadImage(uid)

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)

                    }
                }
        }
    }
    private fun uploadImage(uid: String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val imageRef = storageRef.child(uid+".jpg")
        profileImage.isDrawingCacheEnabled = true
        profileImage.buildDrawingCache()
        val bitmap = (profileImage.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }
}