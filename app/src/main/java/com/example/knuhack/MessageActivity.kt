package com.example.knuhack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.knuhack.dto.ApiResult
import com.example.knuhack.entity.Message
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageActivity : AppCompatActivity() {
    // data
    val items : ArrayList<Message> = ArrayList<Message>()
    var userId : Long = -1
    lateinit var userNickname : String

    val mContext  = this
    lateinit var listView: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actvity_all_message_main)

        intent.getStringExtra("nickname")?.let { userNickname = it }
        userId = intent.getLongExtra("userId", -1)
        listView = findViewById<ListView>(R.id.message_list)
        getMessageList()
    }

    private class MyCustomAdapter(private val items: MutableList<Message>) : BaseAdapter() {

        override fun getCount(): Int = items.size

        override fun getItem(position: Int): Message = items[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.item_message_list_main, null)

            view.findViewById<TextView>(R.id.message_userName).text = items[position].nickname
            view.findViewById<TextView>(R.id.message_date_mainPage).text = items[position].time
            view.findViewById<TextView>(R.id.message_content_mainPage).text = items[position].content

            return view
        }

    }
    private fun getMessageList(){
        RestApiService.instance.getReceived(userId).enqueue(object : Callback<ApiResult<List<Message>>> {
            override fun onResponse(call: Call<ApiResult<List<Message>>>, response: Response<ApiResult<List<Message>>>) {
                items.clear()
                response.body()?.response?.let{
                    items.addAll(it)
                    //        어답터 설정
                    listView.adapter = MyCustomAdapter(items)

                    listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                        val item = parent.getItemAtPosition(position) as Message
                        val intent = Intent(mContext, MessageOneActivity::class.java)

                        intent.putExtra("content",item.content)
                        intent.putExtra("time", item.time)
                        intent.putExtra("otherNickname", item.nickname)
                        intent.putExtra("userId", userId)
                        intent.putExtra("nickname", userNickname)

                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<ApiResult<List<Message>>>, t: Throwable) {
                Toast.makeText(mContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}