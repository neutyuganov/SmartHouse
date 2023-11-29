package com.example.smarthouse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

// Объявление переменной SharedPreferences
private lateinit var sharedPreferences: SharedPreferences

class AddressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adress_activity)

        val window = getWindow()
        window.statusBarColor = this.resources.getColor(R.color.black_primary)

        // Создание переменной кнопки
        var button_save = findViewById<Button>(R.id.button_save)

        // Создание переменной поля для ввода адреса
        var edit_text_address = findViewById<EditText>(R.id.edit_text_address)

        var text_view_error = findViewById<TextView>(R.id.textView_error)

        // Подключение клиента SupaBase
        var clientSB = CreateClientSB().clientSB

        // Создание переменной Vibratior
        var vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // SharedPreferences
        sharedPreferences=getSharedPreferences("SHARED_PREFERENCE",Context.MODE_PRIVATE)

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

        button_save.setOnClickListener {
            if (edit_text_address.text.toString().isEmpty()) {
                text_view_error.setText("Что то пошло не так")
                isVibrationError()
                text_view_error.visibility = TextView.VISIBLE
            } else {
                lifecycleScope.launch {
                    try {

                        // Обновление данных пользователя с добвлением адреса
                        val user =
                            clientSB.gotrue.retrieveUserForCurrentSession(updateSession = true)
                        clientSB.postgrest["Users"].update(
                            {
                                set("address", edit_text_address.text.toString())
                            }
                        ) {
                            eq("id", user.id)
                        }

                        // Сохранение события ввода адреса
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putBoolean("ADDRESS_ADD", true)
                        editor.apply()

                        // Переход на форму ввода пинкода
                        val intent =
                            Intent(this@AddressActivity, MainMenuActivity::class.java)
                        startActivity(intent)
                        this@AddressActivity.finish()

                    } catch (e: Exception) {
                        isVibrationError()
                        text_view_error.setText("Что то пошло не так")
                        text_view_error.visibility = TextView.VISIBLE
                    }
                }
            }

            edit_text_address.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    text_view_error.visibility = TextView.INVISIBLE
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
        }
    }
}
