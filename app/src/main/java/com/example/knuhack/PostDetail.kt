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
import com.example.knuhack.dto.SignInForm
import com.example.knuhack.entity.Board
import com.example.knuhack.entity.Comment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.knuhack.entity.Reply

class PostDetail : AppCompatActivity() {
    val items : ArrayList<CustomAdapter.AdapterItem> = ArrayList<CustomAdapter.AdapterItem>()
    val mContext  = this
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        val content = intent.getStringExtra("content") // Intent에서 Key를 email로 가지고 있는 값 가져오기
        val title= intent.getStringExtra("title")
        val author = intent.getStringExtra("author")

        val commentText = findViewById<EditText>(R.id.input_id) as EditText
        val text1 = findViewById<TextView>(R.id.postTitle) as TextView
        text1.setText(title)
        val text2 = findViewById<TextView>(R.id.detailContent) as TextView
        text2.setText(content)
        val text3 = findViewById<TextView>(R.id.writer) as TextView
        text3.setText(author)

        val commentbtn = findViewById<Button>(R.id.writeCommentBtn) as Button
        commentbtn.setOnClickListener {
            val comment = commentText.text.toString().trim { it <= ' ' }
            writeComment(comment)
        }

        listView = findViewById<ListView>(R.id.commentlistview)

        intent.getLongExtra("boardId",0)?.let {
            getCommentList(it)
        }
    }

    class CustomAdapter(private val items: MutableList<AdapterItem>) : BaseAdapter() {
        data class AdapterItem(
            val author:String,
            val content:String,
            var type:String,
            var commentId : Long
        )

        override fun getCount(): Int = items.size
        override fun getItem(position: Int): AdapterItem = items[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            if(items[position].type.equals("COMMENT"))
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
                Log.e("get board list ", "success get comment")
                items.clear()
                response.body()?.let {
                    Log.e("get board list ", "success response body")


                    it.response?.let{
                        for(comment in it){
                            items.add(CustomAdapter.AdapterItem(comment.author, comment.content, "COMMENT", comment.commentId))
                            for(reply in comment.replyDtoList){
                                items.add(CustomAdapter.AdapterItem(reply.author, reply.content, "REPLY", comment.commentId))
                            }
                        }
                    }

                    //        어답터 설정
                    listView.adapter = CustomAdapter(items)
                }
            }

            override fun onFailure(call: Call<ApiResult<List<Comment>>>, t: Throwable) {
                Toast.makeText(mContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun writeComment(content: String) {
        RestApiService.instance.writeComment().enqueue(object : Callback<ApiResult<String>> {
            override fun onResponse(call: Call<ApiResult<String>>, response: Response<ApiResult<String>>) {
                //내가 할거
                RestApiService.instance.getUserId().enqueue(object : Callback<Long>{
                    override fun onResponse(call: Call<Long>, response: Response<Long>) {
                        Toast.makeText(mContext,"로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                        response.body()?.let { Log.i("get user id", it.toString()) }

                        intent = Intent(mContext, MainActivity::class.java)
                        response.body()?.let { intent.getLongExtra("userId", it) }
                        startActivity(intent)
                        finish()
                    }

                    override fun onFailure(call: Call<Long>, t: Throwable) {
                        t.message?.let { Log.e("failed get user id", it) }
                    }
                })
            }

            override fun onFailure(call: Call<ApiResult<String>>, t: Throwable) {
                Toast.makeText(mContext,"로그인에 x하였습니다.", Toast.LENGTH_SHORT).show()
                t.message?.let { Log.e("login failed", it) }
            }
        })
    }
}