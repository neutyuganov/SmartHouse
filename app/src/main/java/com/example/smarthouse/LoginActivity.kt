package com.example.smarthouse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    // Объявление переменной класса
    private val networkMonitor = NetworkMonitorUtil(this)

    // Объявление переменной SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.login_activity)

        // SharedPreferences
        sharedPreferences=getSharedPreferences("SHARED_PREFERENCE",Context.MODE_PRIVATE)

        //Подключение клиента SupaBase
        val clientSB = CreateClientSB().clientSB

        // Создание переменной Vibratior
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Создание переменной TextView
        val text_view_error: TextView = findViewById(R.id.login_textViewError)

        // Создание переменных EditText
        val edit_text_email: EditText = findViewById(R.id.login_email)
        val edit_text_password: EditText = findViewById(R.id.login_password)

        // Создание переменных Button
        val goLogin: Button = findViewById(R.id.login_button_go_login)
        val goRegistration: Button = findViewById(R.id.login_button_go_reg)

        // Обработчик нажатия кнопки Регистрация
        goRegistration.setOnClickListener{
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        // Функция вибрации 500 мс
        fun isVibrationError(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // API 26
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        500,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                // This method was deprecated in API level 26
                vibrator.vibrate(500)
            }
        }

        // Постоянная проверка подключения к интернету
        networkMonitor.result = { isAvailable, type ->
            runOnUiThread {
                when (isAvailable) {
                    true -> {
                        text_view_error.visibility = TextView.INVISIBLE
                    }
                    false -> {

                    }
                }
            }
        }

        // Функция проверки подключения к интернету
        fun isInternetConnected(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork ?: return false
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
                return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            } else {
                val networkInfo = connectivityManager.activeNetworkInfo ?: return false
                return networkInfo.isConnected
            }
        }

        // Функция валидации логина
        fun isValidEmail(email: String): Boolean {
            val regex = Regex("[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,}")
            return regex.matches(email)
        }

        // Функция проверки полей входа
        fun isEditTextsCheck(){
            //Объявление переменных с данными из EditText
            val email_user = edit_text_email.text.toString()
            val password_user = edit_text_password.text.toString()

            text_view_error.visibility = TextView.INVISIBLE

            if(email_user == "" || password_user == "") {
                goLogin.isEnabled = false
            }
            else goLogin.isEnabled = true
        }

        // Проверка полей ввода после каждого введенного символа в EditText
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

        // Обработчик нажатия кнопки Вход
        goLogin.setOnClickListener{
            try{
                //Объявление переменных с данными из EditText
                val email_user = edit_text_email.text.toString()
                val password_user = edit_text_password.text.toString()

                // Функция Входа
                fun isLogin(){
                    lifecycleScope.launch {
                        try {
                            clientSB.gotrue.loginWith(Email) {
                                email = email_user
                                password = password_user
                            }

                            //внести значение в "PEREMENNAYA"
                            val editor = sharedPreferences.edit()
                            editor.putString("EMAIL", email_user)
                            editor.putString("PASSWORD", password_user)
                            editor.apply()

                            // Переход на форму ввода пинкода
                            val intent =
                                Intent(this@LoginActivity, PinCodeActivity::class.java)
                            startActivity(intent)
                        } catch (e:Exception)
                        {
                            isVibrationError()
                            text_view_error.visibility = TextView.VISIBLE

                            // Проверка условий для вывод ошибок в TextView
                            if(isInternetConnected(this@LoginActivity)){
                                text_view_error.setText("Неверная почта или пароль")
                            }
                            else {

                                text_view_error.setText("Нет подключения к интернету")
                            }
                        }
                    }
                }

                // Проверка условий для вывода ошибок в EditText
                when{
                    !isValidEmail(email_user) && password_user.length < 6 -> {
                        edit_text_email.setError("Неверный формат почты")
                        edit_text_password.setError("Пароль должен быть не меньше 6 символов")
                    }
                    !isValidEmail(email_user) -> edit_text_email.setError("Неверный формат почты")
                    password_user.length < 6 -> edit_text_password.setError("Пароль не может быть меньше 6 символов")
                    isValidEmail(email_user) && password_user.length >= 6 -> isLogin()
                }
            }
            catch (e:Exception) {
                Log.e("ERROR!!!!", e.toString())
            }

        }
    }

    override fun onResume() {
        super.onResume()
        networkMonitor.register()
    }

    override fun onStop() {
        super.onStop()
        networkMonitor.unregister()
    }

}