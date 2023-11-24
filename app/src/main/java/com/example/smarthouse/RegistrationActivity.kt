package com.example.smarthouse

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
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

        // Функция валидации логина
        fun isValidEmail(email: String): Boolean {
            val regex = Regex("[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,}")
            return regex.matches(email)
        }

        // Функция проверки полей регистрации
        fun isEditTextsCheck(){
            //Объявление переменных с данными из EdtText
            val name_user = edit_text_name.text.toString()
            val email_user = edit_text_email.text.toString()
            val password_user = edit_text_password.text.toString()

            if(name_user == "" || email_user == "" || password_user == "") {
                goRegistration.isEnabled = false
            }
            else goRegistration.isEnabled = true
        }

        edit_text_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                isEditTextsCheck()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        edit_text_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                isEditTextsCheck()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        edit_text_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                isEditTextsCheck()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        // Обработчик нажатия кнопки Регистриция
        goRegistration.setOnClickListener{
            try{
                //Объявление переменных с данными из EdtText
                val name_user = edit_text_name.text.toString()
                val email_user = edit_text_email.text.toString()
                val password_user = edit_text_password.text.toString()

                fun isRegistration(){
                    lifecycleScope.launch {
                        clientSB.gotrue.signUpWith(Email) {
                            email = email_user
                            password = password_user
                        }

                        val user =
                            clientSB.gotrue.retrieveUserForCurrentSession(updateSession = true)
                        val userAdd = UserDataClass(id = user.id, name = name_user)
                        clientSB.postgrest["Users"].insert(userAdd)

                        // Переход на форму ввода пинкода
                        val intent =
                            Intent(this@RegistrationActivity, PinCodeActivity::class.java)
                        startActivity(intent)
                    }
                }

                when{
                    !isValidEmail(email_user) -> edit_text_email.setError("Неверный формат почты")
                    password_user.length < 6 -> edit_text_password.setError("Пароль может быть не меньше 6 символов")
                    isValidEmail(email_user) && password_user.length >= 6 -> isRegistration()
                }
            }
            catch (e:Exception) {
                Log.e("ERROR!!!!", e.toString())
            }

        }
    }
}