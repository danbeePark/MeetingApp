package com.example.meetingapp.slider

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meetingapp.R
import com.example.meetingapp.auth.UserDataModel

class CardStackAdapter(val context : Context, val items: List<UserDataModel>) :
    RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater =LayoutInflater.from(parent.context)
        val view : View = inflater.inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.binding(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }


    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView){
            val nickname = itemView.findViewById<TextView>(R.id.itemNickname)
            val age = itemView.findViewById<TextView>(R.id.itemAge)
            val city = itemView.findViewById<TextView>(R.id.itemCity)

        fun binding(data: UserDataModel){
                    nickname.text = data.nickname
                    age.text = data.age
            Log.d("****", ""+data.age)
            Log.d("****", ""+data.city)
            city.text =data.city
            }
    }
}