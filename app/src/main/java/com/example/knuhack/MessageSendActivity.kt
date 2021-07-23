package com.example.knuhack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MessageSendActivity : AppCompatActivity() {
    // data
    var userId: Long = -1
    lateinit var nickname: String

    lateinit var content: String
    lateinit var otherNickname: String

    val mContext = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actvity_send_message_page)

        userId = intent.getLongExtra("userId", -1)
        intent.getStringExtra("nickname")?.let { nickname = it }
        intent.getStringExtra("otherNickname")?.let { otherNickname = it }

        Toast.makeText(mContext, nickname + " " + otherNickname, Toast.LENGTH_SHORT).show()
    }
}