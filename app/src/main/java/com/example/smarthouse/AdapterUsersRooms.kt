package com.example.smarthouse

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class AdapterUsersRooms(
    private val typesList: ArrayList<RoomsMainDataClass>, private val context: Context, private val recyclerView: RecyclerView
): RecyclerView.Adapter<AdapterUsersRooms.MyViewHolder>() {

//    var onItemClick: ((RoomsTypesDataClass) -> Unit)? = null

//    private var pos:Int = 0

    inner class MyViewHolder(itemView_main: View): RecyclerView.ViewHolder(itemView_main) {
        var button: Button = itemView_main.findViewById(R.id.button_room_main)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        return MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val types = typesList[position]

        holder.button.text = types.name
        holder.button.setCompoundDrawablesWithIntrinsicBounds(types.image, null, null, null)

//        holder.itemView.setOnClickListener { // display a toast with person name on item click
//
//            pos = position
//            notifyDataSetChanged()
//        }

    }

    override fun getItemCount(): Int {
        return typesList.size
    }
}