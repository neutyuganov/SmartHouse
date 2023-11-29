package com.example.smarthouse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import org.json.JSONArray

class PinCodeActivity : AppCompatActivity() {

    // Объявление переменной SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences

    var  numberList = ArrayList<String>()

    var passCode = ""
    var pinNum1 = ""
    var pinNum2 = ""
    var pinNum3 = ""
    var pinNum4 = ""

    var pin_indicator1: View? = null
    var pin_indicator2: View? = null
    var pin_indicator3: View? = null
    var pin_indicator4: View? = null

    var head: TextView? = null

    var pin1: Button? = null
    var pin2: Button? = null
    var pin3: Button? = null
    var pin4: Button? = null
    var pin5: Button? = null
    var pin6: Button? = null
    var pin7: Button? = null
    var pin8: Button? = null
    var pin9: Button? = null

    var pinSP: String? = null
    var pinAccesSP: Boolean? = null
    var address_add: Boolean? = null

    lateinit var vibrator: Vibrator

    var clientSB: SupabaseClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.pin_code_activity)

        // Создание переменных индикаторов
        pin_indicator1 = findViewById(R.id.indicator1)
        pin_indicator2 = findViewById(R.id.indicator2)
        pin_indicator3 = findViewById(R.id.indicator3)
        pin_indicator4 = findViewById(R.id.indicator4)

        // Создание переменной заголовка
        head = findViewById(R.id.head)

        //Создание переменных кнопок
        pin1 = findViewById(R.id.button1)
        pin2 = findViewById(R.id.button2)
        pin4 = findViewById(R.id.button4)
        pin3 = findViewById(R.id.button3)
        pin5 = findViewById(R.id.button5)
        pin6 = findViewById(R.id.button6)
        pin7 = findViewById(R.id.button7)
        pin8 = findViewById(R.id.button8)
        pin9 = findViewById(R.id.button9)

        var log_out = findViewById<Button>(R.id.log_out)

        // Создание переменной Vibratior
        vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // SharedPreferences
        sharedPreferences=getSharedPreferences("SHARED_PREFERENCE",Context.MODE_PRIVATE)

        //Подключение клиента SupaBase
        clientSB = CreateClientSB().clientSB

        pinSP = sharedPreferences.getString("PIN", "")
        pinAccesSP = sharedPreferences.getBoolean("PIN_ACCESS", false)

        //получить значение из "ADDRESS_ADD"
        address_add = sharedPreferences.getBoolean("ADDRESS_ADD", false)

        if(pinAccesSP == false){
            head?.text = "Создайте\nпин код"
            log_out.visibility = Button.INVISIBLE
        }
        else{
            head?.text = "Введите\nпин код"
            log_out.visibility = Button.VISIBLE
        }

        log_out.setOnClickListener{
            sharedPreferences.edit().clear().apply()

            // Переход на форму ввода пинкода
            val intent =
                Intent(this@PinCodeActivity, LoginActivity::class.java)
            startActivity(intent)
            this@PinCodeActivity.finish()
        }
    }

    fun passCodeFinish() {
            object : CountDownTimer(250, 50) {
                override fun onTick(l: Long) {
                    pin1?.setEnabled(false)
                    pin2?.setEnabled(false)
                    pin3?.setEnabled(false)
                    pin4?.setEnabled(false)
                    pin5?.setEnabled(false)
                    pin6?.setEnabled(false)
                    pin7?.setEnabled(false)
                    pin8?.setEnabled(false)
                    pin9?.setEnabled(false)
                }

                override fun onFinish() {
                    numberList.clear()
                    pin_indicator1?.setBackgroundResource(R.drawable.pin_empty)
                    pin_indicator2?.setBackgroundResource(R.drawable.pin_empty)
                    pin_indicator3?.setBackgroundResource(R.drawable.pin_empty)
                    pin_indicator4?.setBackgroundResource(R.drawable.pin_empty)

                    pin1?.setEnabled(true)
                    pin2?.setEnabled(true)
                    pin3?.setEnabled(true)
                    pin4?.setEnabled(true)
                    pin5?.setEnabled(true)
                    pin6?.setEnabled(true)
                    pin7?.setEnabled(true)
                    pin8?.setEnabled(true)
                    pin9?.setEnabled(true)
                }
            }.start()
        }

    fun isVibrationClick(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // API 26
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        10,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                // This method was deprecated in API level 26
                vibrator.vibrate(10)
            }
        }

    fun savePassCode(passCode: String): SharedPreferences.Editor? {
            val editor = sharedPreferences.edit()
            editor.putString("PIN", passCode)
            editor.putBoolean("PIN_ACCESS", true)
            editor.apply()
            return editor
    }

    fun matchPassCode() {
            if (pinSP == passCode) {
                pin_indicator1?.setBackgroundResource(R.drawable.access_indicator)
                pin_indicator2?.setBackgroundResource(R.drawable.access_indicator)
                pin_indicator3?.setBackgroundResource(R.drawable.access_indicator)
                pin_indicator4?.setBackgroundResource(R.drawable.access_indicator)
                pin1?.isEnabled = false
                pin2?.isEnabled = false
                pin3?.isEnabled = false
                pin4?.isEnabled = false
                pin5?.isEnabled = false
                pin6?.isEnabled = false
                pin7?.isEnabled = false
                pin8?.isEnabled = false
                pin9?.isEnabled = false
                head?.text  = "Вы успешно\nвошли"

                lifecycleScope.launch {

                    // Обновление данных пользователя с добвлением адреса
                    val user =
                        clientSB?.gotrue?.retrieveUserForCurrentSession(updateSession = true)

                    val addressResponse = clientSB?.postgrest!!["Users"].select(columns = Columns.list("address")){
                        eq("id", user!!.id)
                    }.body.toString()

                    val array = JSONArray(addressResponse)
                    val obj = array.getJSONObject(0)
                    val address = obj.getString("address")

                    if(address.toString() == "null"){
                        // Переход на форму ввода пинкода
                        val intent =
                            Intent(this@PinCodeActivity, AddressActivity::class.java)
                        startActivity(intent)
                        this@PinCodeActivity.finish()
                    }
                    else{
                        // Переход на форму ввода пинкода
                        val intent =
                            Intent(this@PinCodeActivity, MainMenuActivity::class.java)
                        startActivity(intent)
                        this@PinCodeActivity.finish()
                    }
                }

            } else {
                object : CountDownTimer(500, 500) {
                    override fun onTick(l: Long) {
                        vibrator.vibrate(500)
                        pin_indicator1?.setBackgroundResource(R.drawable.error_indicator)
                        pin_indicator2?.setBackgroundResource(R.drawable.error_indicator)
                        pin_indicator3?.setBackgroundResource(R.drawable.error_indicator)
                        pin_indicator4?.setBackgroundResource(R.drawable.error_indicator)
                        pin1?.isEnabled = false
                        pin2?.isEnabled = false
                        pin3?.isEnabled = false
                        pin4?.isEnabled = false
                        pin5?.isEnabled = false
                        pin6?.isEnabled = false
                        pin7?.isEnabled = false
                        pin8?.isEnabled = false
                        pin9?.isEnabled = false
                    }

                    override fun onFinish() {
                        head?.text  = "Введите\nпин код"
                        numberList.clear()
                        pin_indicator1?.setBackgroundResource(R.drawable.pin_empty)
                        pin_indicator2?.setBackgroundResource(R.drawable.pin_empty)
                        pin_indicator3?.setBackgroundResource(R.drawable.pin_empty)
                        pin_indicator4?.setBackgroundResource(R.drawable.pin_empty)
                        pin1?.isEnabled = true
                        pin2?.isEnabled = true
                        pin3?.isEnabled = true
                        pin4?.isEnabled = true
                        pin5?.isEnabled = true
                        pin6?.isEnabled = true
                        pin7?.isEnabled = true
                        pin8?.isEnabled = true
                        pin9?.isEnabled = true
                    }
                }.start()
            }
        }

    fun passCode(numberList: ArrayList<String>) {
        when (this.numberList.size) {
            1 -> {
                pinNum1 = numberList[0]
                pin_indicator1?.setBackgroundResource(R.drawable.pin_fill)
                isVibrationClick()
            }

            2 -> {
                pinNum2 = numberList[1]
                pin_indicator2?.setBackgroundResource(R.drawable.pin_fill)
                isVibrationClick()
            }

            3 -> {
                pinNum3 = numberList[2]
                pin_indicator3?.setBackgroundResource(R.drawable.pin_fill)
                isVibrationClick()
            }

            4 -> {
                pinNum4 = numberList[3]
                pin_indicator4?.setBackgroundResource(R.drawable.pin_fill)
                isVibrationClick()
                passCode = pinNum1 + pinNum2 + pinNum3 + pinNum4
                if (pinSP == "") {
                    savePassCode(passCode)

                    pin_indicator1?.setBackgroundResource(R.drawable.access_indicator)
                    pin_indicator2?.setBackgroundResource(R.drawable.access_indicator)
                    pin_indicator3?.setBackgroundResource(R.drawable.access_indicator)
                    pin_indicator4?.setBackgroundResource(R.drawable.access_indicator)
                    pin1?.isEnabled = false
                    pin2?.isEnabled = false
                    pin3?.isEnabled = false
                    pin4?.isEnabled = false
                    pin5?.isEnabled = false
                    pin6?.isEnabled = false
                    pin7?.isEnabled = false
                    pin8?.isEnabled = false
                    pin9?.isEnabled = false
                    head?.text  = "Вы успешно\nвошли"

                    lifecycleScope.launch {

                        // Обновление данных пользователя с добвлением адреса
                        val user =
                            clientSB?.gotrue?.retrieveUserForCurrentSession(updateSession = true)

                        val addressResponse = clientSB?.postgrest!!["Users"].select(columns = Columns.list("address")){
                            eq("id", user!!.id)
                        }.body.toString()

                        val array = JSONArray(addressResponse)
                        val obj = array.getJSONObject(0)
                        val address = obj.getString("address")

                        if(address.toString() == null){
                            // Переход на форму ввода пинкода
                            val intent =
                                Intent(this@PinCodeActivity, AddressActivity::class.java)
                            startActivity(intent)
                            this@PinCodeActivity.finish()
                        }
                        else{
                            // Переход на форму ввода пинкода
                            val intent =
                                Intent(this@PinCodeActivity, MainMenuActivity::class.java)
                            startActivity(intent)
                            this@PinCodeActivity.finish()
                        }
                    }
                } else {
                    matchPassCode()
                }
            }
        }
    }

    fun onClick(view: View){
        val button = view as Button
        var number = button.text.toString()

        numberList.add(number)
        passCode(numberList)
    }
}