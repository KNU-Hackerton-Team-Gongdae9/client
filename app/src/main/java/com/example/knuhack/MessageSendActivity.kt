package com.example.knuhack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.knuhack.dto.ApiResult
import com.example.knuhack.dto.MessageForm
import com.example.knuhack.entity.Message
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageSendActivity : AppCompatActivity() {
    // data
    var userId: Long = -1
    lateinit var nickname: String
    lateinit var otherNickname: String

    val mContext = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actvity_send_message_page)

        userId = intent.getLongExtra("userId", -1)
        intent.getStringExtra("nickname")?.let { nickname = it }
        intent.getStringExtra("otherNickname")?.let { otherNickname = it }

        Toast.makeText(mContext, nickname + " " + otherNickname, Toast.LENGTH_SHORT).show()

        findViewById<Button>(R.id.message_button_writeMessage).setOnClickListener {
            val content = findViewById<EditText>(R.id.message_input_writeMessage).text.toString().trim { it <= ' ' }
            sendMessage(userId, otherNickname, content)
        }
    }


    private fun sendMessage(fromId:Long, toNickname: String, content: String) {
        Log.e("asdf", fromId.toString() + " " + toNickname + " " + content)

        RestApiService.instance.send(MessageForm(content), fromId, toNickname).enqueue(object : Callback<ApiResult<String>> {
            override fun onResponse(call: Call<ApiResult<String>>, response: Response<ApiResult<String>>) {
                response.body()?.response?.let {
                    Log.e("message", it)
                    finish()
                }
            }

            override fun onFailure(call: Call<ApiResult<String>>, t: Throwable) {
                Toast.makeText(mContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}