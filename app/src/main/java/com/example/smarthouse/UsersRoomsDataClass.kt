package com.example.smarthouse

import kotlinx.serialization.Serializable

@Serializable
data class UsersRoomsDataClass(val user_id:String, val name:String, val rooms_type_id:Int) {
}