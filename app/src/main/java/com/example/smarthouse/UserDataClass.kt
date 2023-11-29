package com.example.smarthouse

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import kotlinx.serialization.Serializable

@Serializable
data class UserDataClass(val id: String = "", val name: String = "", val address: String = "", val avatar: String = ""){
}