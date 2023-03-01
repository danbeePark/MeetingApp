package com.example.meetingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.meetingapp.auth.IntroActivity
import com.example.meetingapp.auth.UserDataModel
import com.example.meetingapp.setting.SettingActivity
import com.example.meetingapp.slider.CardStackAdapter
import com.example.meetingapp.utils.FirebaseAuthUtils
import com.example.meetingapp.utils.FirebaseRef
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class MainActivity : AppCompatActivity() {

    private var TAG = "MainActivity"
    private val usersDataList = mutableListOf<UserDataModel>()
    lateinit var  cardStackAdapter: CardStackAdapter
    lateinit var manager : CardStackLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val setting = findViewById<ImageView>(R.id.settingIcon)
        getUserDataList()
        setting.setOnClickListener {
            val auth =Firebase.auth
           // auth.signOut()
            val intent = Intent(this, SettingActivity::class.java)

            startActivity(intent)
        }
        val cardStackView = findViewById<CardStackView>(R.id.cardStackView)
        manager = CardStackLayoutManager(baseContext, object: CardStackListener{

            override fun onCardDragging(direction: Direction?, ratio: Float) {

            }

            override fun onCardSwiped(direction: Direction?) {

            }

            override fun onCardRewound() {

            }

            override fun onCardCanceled() {

            }

            override fun onCardAppeared(view: View?, position: Int) {

            }

            override fun onCardDisappeared(view: View?, position: Int) {

            }

        })


        cardStackAdapter = CardStackAdapter(baseContext, usersDataList)
        cardStackView.layoutManager = manager
        cardStackView.adapter = cardStackAdapter
    }

    private fun getUserDataList(){
        // Read from the database
        FirebaseRef.userInfoRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //val value = dataSnapshot.getValue<String>()
                //val value = dataSnapshot.toString()
                for(dataModel in dataSnapshot.children){

                    val user=dataModel.getValue(UserDataModel::class.java)
                     usersDataList.add(user!!)
                }

                cardStackAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

    }
}