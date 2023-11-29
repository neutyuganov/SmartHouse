package com.example.smarthouse

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.InputStream

class AddRoomActivity : AppCompatActivity() {

    val clientSB = CreateClientSB().clientSB
    var array: JSONArray? = null
    var viewItems: ArrayList<RoomsTypesDataClass> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_room_activity)



        val recyclerView: RecyclerView = findViewById(R.id.recycler_rooms_types)

        lifecycleScope.launch {
            val rooms_types = clientSB.postgrest["Rooms_types"].select()

            val buf_client = StringBuilder()

            buf_client.append(rooms_types.body.toString()).append("\n")

            array = JSONArray(buf_client.toString())

            addItemsFromJSON()
        }

        val timer = object: CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                val adapter = AdapterRoomsType(viewItems,this@AddRoomActivity,recyclerView)
                recyclerView.adapter = adapter
            }
        }
        timer.start()
    }

    private fun addItemsFromJSON() {
        try {
// Заполняем Модель спаршенными данными
            for (i in 0 until array!!.length()) {
                val itemObj: JSONObject = array!!.getJSONObject(i)
                val id = itemObj.getString("id")
                val name = itemObj.getString("name")
                val avatar = itemObj.getString("image_white")
                lifecycleScope.launch {
                    try {
                        val bucket = clientSB.storage["images"]
                        val bytes = bucket.downloadPublic(avatar)
                        val is1: InputStream = ByteArrayInputStream(bytes)
                        val bmp: Bitmap = BitmapFactory.decodeStream(is1)
                        val dr = BitmapDrawable(resources,bmp)
                        val phones = RoomsTypesDataClass(id, name,avatar, dr)
                        viewItems.add(phones)
                    }catch (e:Exception){
                        Log.e("MESSAGE",e.toString())
                    }
                }
            }
        } catch (e: JSONException) {
        }
    }
}