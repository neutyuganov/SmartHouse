package com.example.smarthouse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import org.json.JSONArray

class MainMenuActivity : AppCompatActivity() {

    // Объявление переменной SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu_activity)

        val window = getWindow()
        window.statusBarColor = this.resources.getColor(R.color.black_primary)

        val clientSB = CreateClientSB().clientSB

        // Добавление переменных Button
        val settings = findViewById<ImageButton>(R.id.button_settings)
        val add_room = findViewById<ImageButton>(R.id.button_add)

        // Добавление переменной TextView
        val address = findViewById<TextView>(R.id.textViewAddress)

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

//        lifecycleScope.launch {
//
//            val user = clientSB.gotrue.retrieveUserForCurrentSession(updateSession = true)
//
//            val response = clientSB.postgrest["Users"].select(columns = Columns.list("address")){
//                eq("id", user.id)
//            }.decodeSingle<UserDataClass>()
//
//            address.text = response.address
//        }



        // Переход на ProfileActivity
        settings.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        // Переход на AddRoomActivity
        add_room.setOnClickListener{
            val intent = Intent(this, AddRoomActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }
}