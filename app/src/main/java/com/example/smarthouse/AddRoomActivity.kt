package com.example.smarthouse

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import io.github.jan.supabase.gotrue.gotrue
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

    var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_room_activity)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_rooms_types)

        val save_room = findViewById<Button>(R.id.button_save_room)
        val back = findViewById<ImageButton>(R.id.button_back_room)
        val name_room = findViewById<TextView>(R.id.edit_text_name_room)

        val adapter = AdapterRoomsType(viewItems,this@AddRoomActivity,recyclerView)

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

                recyclerView.adapter = adapter
            }
        }
        timer.start()

        adapter.onItemClick = {
            id = it.id
            if(id == "" || name_room.text.toString() == ""){
                save_room.isEnabled = false
            }
            else save_room.isEnabled = true
        }

        name_room.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(id == "" || name_room.text.toString() == ""){
                    save_room.isEnabled = false
                }
                else save_room.isEnabled = true
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        back.setOnClickListener{
            onBackPressed()
        }

        save_room.setOnClickListener{
            lifecycleScope.launch {
                // Ввод данных в таблицу Users
                val user =
                    clientSB.gotrue.retrieveUserForCurrentSession(updateSession = true)
                val roomAdd = UsersRoomsDataClass(user_id = user.id, name = name_room.text.toString(), rooms_type_id = id.toInt())
                clientSB.postgrest["Rooms"].insert(roomAdd)

                onBackPressed()
            }
        }

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