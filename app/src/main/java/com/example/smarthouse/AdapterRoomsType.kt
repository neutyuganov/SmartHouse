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

    var onItemClick: ((RoomsTypesDataClass) -> Unit)? = null

    private var pos:Int = 0

    private var flag:Int = 0

    private val onColor = ContextCompat.getColor(context,R.color.blue_primary)
    private val offColor = ContextCompat.getColor(context,R.color.grey)

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name)
        var image: ImageView = itemView.findViewById(R.id.photo)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_round, parent, false)
        return MyViewHolder(v)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val types = typesList[position]

        if(flag == 0){

        }
        else{
            if(position==pos){
                holder.name.setTextColor(onColor)
                holder.image.setBackgroundResource(R.drawable.button_round_big_blue)
            }
            else{
                holder.name.setTextColor(offColor)
                holder.image.setBackgroundResource(R.drawable.button_round_big_grey)
            }
        }

        holder.name.text = types.name
        holder.image.setImageDrawable(types.image)

        holder.itemView.setOnClickListener { // display a toast with person name on item click
            onItemClick?.invoke(types)

            flag = 1

            pos = position
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return typesList.size
    }
}