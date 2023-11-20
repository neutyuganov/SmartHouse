package com.example.smarthouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.registration_activity)

        val goLogin: Button = findViewById(R.id.registration_button_go_login)
        val goRegistration: Button = findViewById(R.id.registration_button_go_reg)

        goLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        goRegistration.setOnClickListener{
            val intent = Intent(this, PinCodeActivity::class.java)
            startActivity(intent)
        }
    }
}