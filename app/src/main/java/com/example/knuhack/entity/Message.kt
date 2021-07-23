package com.example.knuhack.entity

import android.provider.ContactsContract


data class Message(
    var nickname: String,
    var otherNickname: String,
    var content : String,
    var time : String
)
