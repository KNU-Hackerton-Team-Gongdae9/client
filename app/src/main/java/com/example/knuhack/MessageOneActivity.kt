package com.example.knuhack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ListView
import android.widget.Toast
import com.example.knuhack.entity.Message

class MessageOneActivity : AppCompatActivity() {
    // data
    val items : ArrayList<Message> = ArrayList<Message>()
    var userId : Long = -1
    lateinit var nickname : String

    lateinit var content : String
    lateinit var otherNickname: String
    lateinit var time : String

    val mContext  = this
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actvity_one_message_page)

        userId = intent.getLongExtra("userId", -1)
        intent.getStringExtra("nickname")?.let { nickname = it }

        intent.getStringExtra("content")?.let { content = it }
        intent.getStringExtra("otherNickname")?.let { otherNickname = it }
        intent.getStringExtra("time")?.let { time = it }

        Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show()
    }
}