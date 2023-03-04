package com.example.meetingapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
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
import com.google.firebase.storage.ktx.storage
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class MainActivity : AppCompatActivity() {

    private var TAG = "MainActivity"
    private val usersDataList = mutableListOf<UserDataModel>()
    lateinit var  cardStackAdapter: CardStackAdapter
    lateinit var manager : CardStackLayoutManager
    private var userCount =0
    private var uid = ""
    private var mygender = ""
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
                if(direction == Direction.Right){
                   // Toast.makeText(this@MainActivity, "right" ,Toast.LENGTH_SHORT).show()
                 //   Toast.makeText(this@MainActivity, usersDataList[userCount].uid.toString() ,Toast.LENGTH_SHORT).show()
                    userLikeOtherUser(uid ,  usersDataList[userCount].uid.toString())
                }
                if(direction == Direction.Left){
                   // Toast.makeText(this@MainActivity, "Left" ,Toast.LENGTH_SHORT).show()
                }

                userCount = userCount + 1
                if(userCount == usersDataList.count() ){
                    getUserDataList()
                }
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
    private fun myUserData(){
        uid = FirebaseAuthUtils.getUid()

        FirebaseRef.userInfoRef.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val data = dataSnapshot.getValue(UserDataModel::class.java)
                mygender = data?.gender.toString()
                mygender = mygender.uppercase()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun getUserDataList(){
        // Read from the database
        myUserData()

        FirebaseRef.userInfoRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //val value = dataSnapshot.getValue<String>()
                //val value = dataSnapshot.toString()
                for(dataModel in dataSnapshot.children){

                    val user=dataModel.getValue(UserDataModel::class.java)
                    if(((user!!.gender)?.uppercase()).equals(mygender)){
                       // Toast.makeText(this@MainActivity, "같은성별" ,Toast.LENGTH_SHORT).show()

                    }
                      else{
                            usersDataList.add(user!!)
                    }
                }

                cardStackAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

    }

    //어떤 데이터를 저장하는지?
    //나의  uid/ 상대방의 uid
    private fun userLikeOtherUser(myUid: String, otherUid : String){
        //Toast.makeText(this@MainActivity, "$myUid,$otherUid",Toast.LENGTH_SHORT).show()
        FirebaseRef.userLikeRef.child(myUid).child(otherUid).setValue("good")
        getOtherUsersLikeList(otherUid)
    }

    private fun getOtherUsersLikeList(otherUid : String){
        FirebaseRef.userLikeRef.child(otherUid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

               // val data = dataSnapshot.getValue(UserDataModel::class.java)
               //내가 좋아 하는 사람의 좋아요 리스트, 이 안에서, 나의 uid가있는지 확인
                for(dataModel in dataSnapshot.children){
                   val likeUserKey = dataModel.key.toString()
                   if(likeUserKey == uid){
                       Toast.makeText(this@MainActivity, "매칭완료",
                       Toast.LENGTH_SHORT).show()

                       createNoti()
                       sendNoti()
                   }
               }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }



    private fun createNoti(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name  //getString(R.string.channel_name)
                ="channel_name"
            val descriptionText  //getString(R.string.channel_description)
                ="channel_description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

    private fun sendNoti(){
        val notification = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("title")
            .setContentText("content")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        with(NotificationManagerCompat.from(this)) {
            notify(11, notification)
        }
    }
}