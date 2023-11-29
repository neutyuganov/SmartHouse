package com.example.smarthouse

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class AdapterRoomsType(
    private val typesList: ArrayList<RoomsTypesDataClass>, private val context: Context, private val recyclerView: RecyclerView
): RecyclerView.Adapter<AdapterRoomsType.MyViewHolder>() {
    private var pos:Int = 0
    private val onColor = ContextCompat.getColor(context,R.color.blue_primary)
    private val offColor = ContextCompat.getColor(context,R.color.grey)

//    private val onDrawable = getDrawable(context,R.drawable.button_round_big_blue)
//    private val offDrawable = getDrawable(context,R.drawable.button_round_big_grey)

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var id: TextView = itemView.findViewById(R.id.id)
        var name: TextView = itemView.findViewById(R.id.name)
        var avatar: TextView = itemView.findViewById(R.id.avatar)
        var image: ImageView = itemView.findViewById(R.id.photo)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_round, parent, false)
        return MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val types = typesList[position]

        holder.id.text = types.id.toString()
        holder.name.text = types.name.toString()
        holder.avatar.text = types.image_white.toString()
        holder.image.setImageDrawable(types.image)
        if(position==pos){
            holder.name.setTextColor(onColor)
            holder.image.setBackgroundResource(R.drawable.button_round_big_blue)
        }
        else{
            holder.name.setTextColor(offColor)
            holder.image.setBackgroundResource(R.drawable.button_round_big_grey)
        }
        holder.itemView.setOnClickListener { // display a toast with person name on item click
            Toast.makeText(context, types.id, Toast.LENGTH_SHORT).show()
            pos = position
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return typesList.size
    }
}