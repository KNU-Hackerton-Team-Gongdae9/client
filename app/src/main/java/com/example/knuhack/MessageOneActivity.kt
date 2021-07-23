package com.example.knuhack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.knuhack.dto.ApiResult
import com.example.knuhack.entity.Message
import kotlinx.android.synthetic.main.activity_post_list.*
import kotlinx.android.synthetic.main.actvity_one_message_page.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageOneActivity : AppCompatActivity() {
    // data
    var userId: Long = -1
    lateinit var nickname: String

    lateinit var content: String
    lateinit var otherNickname: String
    lateinit var time: String

    val mContext = this
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actvity_one_message_page)

        userId = intent.getLongExtra("userId", -1)
        intent.getStringExtra("nickname")?.let { nickname = it }

        intent.getStringExtra("content")?.let { content = it }
        intent.getStringExtra("otherNickname")?.let { otherNickname = it }
        intent.getStringExtra("time")?.let { time = it }

        message_send_btn.setOnClickListener {
            intent = Intent(mContext, MessageSendActivity::class.java)
            intent.putExtra("nickname", nickname)
            intent.putExtra("otherNickname", otherNickname)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        listView = findViewById<ListView>(R.id.message_one_list)
        getMessageList()
    }


    override fun onResume() {
        super.onResume()
        getMessageList()
    }

    private class MyCustomAdapter(val otherNickname: String, private val items: List<Message>) : BaseAdapter() {

        override fun getCount(): Int = items.size

        override fun getItem(position: Int): Message = items[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            if (items[position].otherNickname.equals(otherNickname)) {
                val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.item_message_main_list_sender, null)

                view.findViewById<TextView>(R.id.sender_message_content).text = items[position].content
                view.findViewById<TextView>(R.id.sender_message_date).text = items[position].time

                return view
            } else {
                val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.item_message_main_list_receiver, null)

                view.findViewById<TextView>(R.id.my_message_content).text = items[position].content
                view.findViewById<TextView>(R.id.my_message_date).text = items[position].time

                return view
            }
        }
    }

    private fun getMessageList() {
        RestApiService.instance.getChatList(nickname, otherNickname).enqueue(object : Callback<ApiResult<List<Message>>> {
            override fun onResponse(call: Call<ApiResult<List<Message>>>, response: Response<ApiResult<List<Message>>>) {
                response.body()?.response?.let {
                    Log.e("asdf", it.toString())
                    listView.adapter = MyCustomAdapter(otherNickname, it)
                }
            }

            override fun onFailure(call: Call<ApiResult<List<Message>>>, t: Throwable) {
                Toast.makeText(mContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


}