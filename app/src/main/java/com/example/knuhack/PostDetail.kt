package com.example.knuhack

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.knuhack.dto.ApiResult
import com.example.knuhack.entity.Board
import com.example.knuhack.entity.Comment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.knuhack.entity.Reply

class PostDetail : AppCompatActivity() {
    val items : ArrayList<Comment> = ArrayList<Comment>()
    val mContext  = this
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        intent.getLongExtra("boardId",0)?.let { getCommentList(it) }

        val content = intent.getStringExtra("content") // Intent에서 Key를 email로 가지고 있는 값 가져오기
        val title= intent.getStringExtra("title")
        val author = intent.getStringExtra("author")

        val text1 = findViewById<TextView>(R.id.postTitle) as TextView
        text1.setText(title)
        val text2 = findViewById<TextView>(R.id.detailContent) as TextView
        text2.setText(content)
        val text3 = findViewById<TextView>(R.id.writer) as TextView
        text3.setText(author)

        //댓글
        val items = mutableListOf<test>()

        listView = findViewById<ListView>(R.id.commentlistview)

        listView.adapter = CustomAdapter(items)

    }

    private class CustomAdapter(private val items: MutableList<Comment>) : BaseAdapter() {

        override fun getCount(): Int = items.size
        override fun getItem(position: Int): Comment = items[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            if(items[position].type==1)
            {
                val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.item_comment_list, null)
                val author = view.findViewById<TextView>(R.id.userNickname)
                author.text = items[position].author
                val content = view.findViewById<TextView>(R.id.contents)
                content.text = items[position].content

                return view
            }
            else
            {
                val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.item_reply_list, null)
                val author = view.findViewById<TextView>(R.id.userNickname)
                author.text = items[position].author
                val content = view.findViewById<TextView>(R.id.contents)
                content.text = items[position].content

                return view
            }

        }
    }

    private fun getCommentList(boardId : Long){
        RestApiService.instance.findContentsByBoardId(boardId).enqueue(object : Callback<ApiResult<List<Comment>>>{
            override fun onResponse(call: Call<ApiResult<List<Comment>>>, response: Response<ApiResult<List<Comment>>>) {
                items.clear()

                response.body()?.let {
                    Log.i("get board list ", it.response.toString())
                    items.addAll(it.response)
                    //        어답터 설정
                    listView.adapter = CustomAdapter(items)
                }
            }

            override fun onFailure(call: Call<ApiResult<List<Comment>>>, t: Throwable) {
                Toast.makeText(mContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}