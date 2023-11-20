package com.example.smarthouse

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.login_activity)

        val goLogin: Button = findViewById(R.id.login_button_go_login)
        val goRegistration: Button = findViewById(R.id.login_button_go_reg)

        goRegistration.setOnClickListener{
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        goLogin.setOnClickListener{
            val intent = Intent(this, PinCodeActivity::class.java)
            startActivity(intent)
        }
    }
}