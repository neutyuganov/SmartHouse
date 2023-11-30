package com.example.smarthouse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import java.io.ByteArrayInputStream
import java.io.InputStream

class MainMenuActivity : AppCompatActivity() {

    // Объявление переменной SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences

    val clientSB = CreateClientSB().clientSB

    var array_rooms: JSONArray? = null
    var array_rooms_types: JSONArray? = null
    var viewItems_main: ArrayList<RoomsMainDataClass> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu_activity)

        val window = getWindow()
        window.statusBarColor = this.resources.getColor(R.color.black_primary)

        // Добавление переменных Button
        val settings = findViewById<ImageButton>(R.id.button_settings)
        val add_room = findViewById<ImageButton>(R.id.button_add)

        // Добавление переменной TextView
        val address = findViewById<TextView>(R.id.textViewAddress)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_main)

        val adapter = AdapterUsersRooms(viewItems_main,this@MainMenuActivity,recyclerView)

        // Внесение данных в переменную SharedPreferences
        sharedPreferences=getSharedPreferences("SHARED_PREFERENCE", Context.MODE_PRIVATE)

        val user_id = sharedPreferences.getString("USER_ID", "")

        lifecycleScope.launch {

            val addressResponse = clientSB.postgrest["Users"].select(columns = Columns.list("address")){
                eq("id", user_id!!)
            }.body.toString()

            val array = JSONArray(addressResponse)
            val obj = array.getJSONObject(0)
            val address1 = obj.getString("address")

            address.text = address1.toString()
        }

        lifecycleScope.launch {

            val users_rooms = clientSB.postgrest["Rooms"].select() {
                eq("user_id", user_id!!)
            }

            val rooms_types = clientSB.postgrest["Rooms_types"].select()

            val buf_users_rooms =StringBuilder()
            val buf_rooms_types =StringBuilder()

            buf_users_rooms.append(users_rooms.body.toString()).append("\n")
            buf_rooms_types.append(rooms_types.body.toString()).append("\n")

            array_rooms = JSONArray(buf_users_rooms.toString())
            array_rooms_types = JSONArray(buf_rooms_types.toString())

            addItemsFromJSON()
        }



        // Переход на ProfileActivity
        settings.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // Переход на AddRoomActivity
        add_room.setOnClickListener{
            val intent = Intent(this, AddRoomActivity::class.java)
            startActivity(intent)
        }

        val timer = object: CountDownTimer(8000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {

                recyclerView.adapter = adapter
            }
        }
        timer.start()
    }


    private fun addItemsFromJSON() {
        try {
            // Заполняем Модель спаршенными данными
            for (i in 0 until array_rooms!!.length()) {
                val itemObj = array_rooms!!.getJSONObject(i)
                Log.e("!", itemObj.toString())
                val room_id = itemObj.getString("id")
                val room_name = itemObj.getString("name")
                val room_type = itemObj.getString("rooms_type_id").toInt()
                val itemObjId = array_rooms_types!!.getJSONObject(room_type)
                Log.e("!!", itemObjId.toString())
                val image_blue = itemObjId.getString("image_blue")
                lifecycleScope.launch {
                    try {
                        val bucket = clientSB.storage["images"]
                        val bytes = bucket.downloadPublic(image_blue)
                        val is1: InputStream = ByteArrayInputStream(bytes)
                        val bmp: Bitmap = BitmapFactory.decodeStream(is1)
                        val dr = BitmapDrawable(resources,bmp)
                        val catalogs = RoomsMainDataClass(room_id, room_name, image_blue, dr)
                        Log.e("!!!", catalogs.toString())
                        viewItems_main.add(catalogs)
                    }catch (e:Exception){
                        Log.e("MESSAGE",e.toString())
                    }
                }
            }
        } catch (e: JSONException) {
            Log.e("ERROR!", e.toString())
        }
    }
}