package com.example.knuhack

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.knuhack.dto.ApiResult
import com.example.knuhack.dto.CommentForm
import com.example.knuhack.entity.Comment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetail : AppCompatActivity() {
    val items : ArrayList<CustomAdapter.AdapterItem> = ArrayList<CustomAdapter.AdapterItem>()
    val mContext  = this
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        val boardId = intent.getLongExtra("boardId",0)
        val content = intent.getStringExtra("content") // Intent에서 Key를 email로 가지고 있는 값 가져오기
        val title= intent.getStringExtra("title")
        val author = intent.getStringExtra("author")
        var myNickname = intent.getStringExtra("myNickname")

        val commentText = findViewById<EditText>(R.id.commentEdit) as EditText
        val text1 = findViewById<TextView>(R.id.postTitle) as TextView
        text1.setText(title)
        val text2 = findViewById<TextView>(R.id.detailContent) as TextView
        text2.setText(content)
        val text3 = findViewById<TextView>(R.id.writer) as TextView
        text3.setText(author)

        val commentbtn = findViewById<Button>(R.id.writeCommentBtn) as Button

        commentbtn.setOnClickListener {
            val content = commentText.text.toString().trim { it <= ' ' }
            Log.i("가져온 텍스트 : ", content)
            if (myNickname != null) {
                writeComment(boardId, myNickname, content, commentText)
            }
        }

        listView = findViewById<ListView>(R.id.commentlistview)

        getCommentList(boardId)
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

    private fun writeComment(boardId: Long, nickname: String, content: String, commentText: EditText) {
        RestApiService.instance.writeComment(boardId, CommentForm(content, nickname)).enqueue(object : Callback<ApiResult<Comment>> {
            override fun onResponse(call: Call<ApiResult<Comment>>, response: Response<ApiResult<Comment>>) {
                response.body()?.let {
                    Log.i("댓글 작성이 성공적으로 수행되었습니다.", it.toString())
                    commentText.setText("")
                }
            }

            override fun onFailure(call: Call<ApiResult<Comment>>, t: Throwable) {
                Toast.makeText(mContext,"댓글 작성에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                t.message?.let { Log.e("comment writing failed", it) }
            }
        })
    }
}