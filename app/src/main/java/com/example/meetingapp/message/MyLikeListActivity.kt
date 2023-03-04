package com.example.meetingapp.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.meetingapp.R
import com.example.meetingapp.auth.UserDataModel
import com.example.meetingapp.utils.FirebaseAuthUtils
import com.example.meetingapp.utils.FirebaseRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MyLikeListActivity : AppCompatActivity() {
   private val TAG ="MyLikeListActivity"
    private val uid = FirebaseAuthUtils.getUid()

    private val likeUsersListUid = mutableListOf<String>()
    private val likeUsersList = mutableListOf<UserDataModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_like_list)
        //내가 좋아하는 사람들의 정보
        getMyLikeList(uid)
    }

    private fun getMyLikeList(uid : String){
        FirebaseRef.userLikeRef.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // val data = dataSnapshot.getValue(UserDataModel::class.java)
                //내가 좋아 하는 사람의 좋아요 리스트, 이 안에서, 나의 uid가있는지 확인
                for(dataModel in dataSnapshot.children){
                  //Log.d(TAG, dataModel.key.toString())
                    //내가 좋아하는 사람들의 리스트
                    likeUsersListUid.add(dataModel.key.toString())
                }
                getUserDataList()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getUserDataList(){
        // Read from the database

        FirebaseRef.userInfoRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //val value = dataSnapshot.getValue<String>()
                //val value = dataSnapshot.toString()
                for(dataModel in dataSnapshot.children){

                    val user=dataModel.getValue(UserDataModel::class.java)

                    if(likeUsersListUid.contains(user?.uid)){
                        likeUsersList.add(user!!)
                    }
                }
            }


            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

    }
}