package com.example.smarthouse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email

import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    // Объявление переменной SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.splash_screen_activity)

        //Подключение клиента SupaBase
        val clientSB = CreateClientSB().clientSB

        lifecycleScope.launch {
            clientSB.gotrue.loginWith(Email){
                email = "utyuganovsergey55@gmail.com"
                password = "123456"
            }
        }


//        // Внесение данных в переменную SharedPreferences
//        sharedPreferences=getSharedPreferences("SHARED_PREFERENCE", Context.MODE_PRIVATE)
//
//        //получить значение из "PEREMENNAYA"
//        var email = sharedPreferences.getString("EMAIL","")
//        var password = sharedPreferences.getString("PASSWORD","")
//
//        // Функция проверки подключения к интернету
//        fun isInternetConnected(context: Context): Boolean {
//            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                val network = connectivityManager.activeNetwork ?: return false
//                val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
//                return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//            } else {
//                val networkInfo = connectivityManager.activeNetworkInfo ?: return false
//                return networkInfo.isConnected
//            }
//        }
//
//        if((email.equals("") && password.equals(""))){
//            object : CountDownTimer(3000, 1000) {
//                override fun onTick(l: Long) {}
//
//                override fun onFinish() {
//                    // Переход на форму ввода Логина
//                    val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
//                    startActivity(intent)
//                }
//            }.start()
//        }
//        else{
//            if(!isInternetConnected(this@SplashScreenActivity)){
//                object : CountDownTimer(3000, 1000) {
//                    override fun onTick(l: Long) {}
//
//                    override fun onFinish() {
//                        // Переход на форму ввода Логина
//                        val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
//                        startActivity(intent)
//                    }
//                }.start()
//            }
//            else{
//                lifecycleScope.launch {
//                    clientSB.gotrue.signUpWith(Email) {
//                        email = "utyuganovsergey551@gmail.com"
//                        password = "1234561"
//                    }
//                    // Переход на форму ввода Логина
//                    val intent = Intent(this@SplashScreenActivity, PinCodeActivity::class.java)
//                    startActivity(intent)
//                }
//            }
//        }
    }
}