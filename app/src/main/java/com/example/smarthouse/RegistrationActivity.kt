package com.example.smarthouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.registration_activity)

        //Подключение клиента SupaBase
        val clientSB = CreateClientSB().clientSB

        // Создание переменных EditText
        val edit_text_name: EditText = findViewById(R.id.registration_name)
        val edit_text_email: EditText = findViewById(R.id.registration_email)
        val edit_text_password: EditText = findViewById(R.id.registration_password)

        // Создание переменных Button
        val goLogin: Button = findViewById(R.id.registration_button_go_login)
        val goRegistration: Button = findViewById(R.id.registration_button_go_reg)

        // Обработчик нажатия кнопки Войти
        goLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Обработчик нажатия кнопки Регистриция
        goRegistration.setOnClickListener{
            lifecycleScope.launch {

                try{

                    //Объявление переменных с данными из EdtText
                    val name_user = edit_text_name.text.toString()
                    val email_user = edit_text_email.text.toString()
                    val password_user = edit_text_password.text.toString()

                    // Метод валидации логина
                    fun isValidEmail(email: String): Boolean {
                        val regex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
                        return regex.matches(email)
                    }

                    if(isValidEmail(email_user)){
                        clientSB.gotrue.signUpWith(Email){
                            email = email_user
                            password = password_user
                        }

                        val user = clientSB.gotrue.retrieveUserForCurrentSession(updateSession = true)
                        val userAdd =  UserDataClass(id = user.id, name = name_user)
                        clientSB.postgrest["Users"].insert(userAdd)

                        // Переход на форму ввода пинкода
                        val intent = Intent(this@RegistrationActivity, PinCodeActivity::class.java)
                        startActivity(intent)
                    }
                    else Toast.makeText(this@RegistrationActivity,"Логин в неверном формате", Toast.LENGTH_SHORT).show()

                }
                catch (e:Exception) {
                    Log.e("ERROR!!!!", e.toString())
                }

            }
        }
    }
}