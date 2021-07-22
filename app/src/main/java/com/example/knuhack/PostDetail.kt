package com.example.knuhack

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
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
    val mContext  = this

    // user data
    var userId : Long = -1
    lateinit var userNickname : String

    // board data
    val items : ArrayList<CustomAdapter.AdapterItem> = ArrayList<CustomAdapter.AdapterItem>()
    lateinit var content: String
    lateinit var tile: String
    lateinit var author: String
    var boardId : Long = -1

    // view
    lateinit var listView: ListView

    // board
    lateinit var commentTextView : EditText
    lateinit var titleTextView : TextView
    lateinit var detailTextView : TextView
    lateinit var authorTextView: TextView


    // button
    lateinit var writeCommentBtn : Button
    lateinit var toAuthorBtn : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        setDataWithIntent()
        findViews()
        setClickListeners()

        getCommentList(boardId)
    }

    private fun setDataWithIntent() {
        boardId = intent.getLongExtra("boardId",0)
        userId = intent.getLongExtra("userId", -1)
        intent.getStringExtra("nickname")?.let { userNickname = it }

        intent.getStringExtra("content")?.let { content = it }  // Intent에서 Key를 email로 가지고 있는 값 가져오기
        intent.getStringExtra("title")?.let { title = it }
        intent.getStringExtra("author")?.let { author = it }
    }

    private fun findViews(){
        commentTextView = findViewById<EditText>(R.id.commentEdit) as EditText
        titleTextView = findViewById<TextView>(R.id.postTitle) as TextView
        titleTextView.setText(title)
        detailTextView= findViewById<TextView>(R.id.detailContent) as TextView
        detailTextView.setText(content)
        authorTextView = findViewById<TextView>(R.id.writer_postDetail) as TextView
        authorTextView.setText(author)

        listView = findViewById<ListView>(R.id.commentlistview)
        writeCommentBtn = findViewById<Button>(R.id.writeCommentBtn)
        toAuthorBtn = findViewById<ImageButton>(R.id.menu_btn_postDetail)
    }

    private fun setClickListeners(){


        writeCommentBtn.setOnClickListener {
            val content = commentTextView.text.toString().trim { it <= ' ' }
            Log.i("가져온 텍스트 : ", content)
            if (userNickname != null) {
                writeComment(boardId, userNickname, content, commentTextView)
            }
        }

        toAuthorBtn.setOnClickListener {
            setAuthorDialog(author)
        }
    }

    private fun setAuthorDialog(author: String){
        val oItems = listOf<CharSequence>("프로필 보기", "쪽지 보내기").toTypedArray()
        val oDialog = AlertDialog.Builder(mContext, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
        oDialog.setTitle("무엇을 하시겠어요?").setItems(oItems, DialogInterface.OnClickListener { dialog, which ->
            when(oItems[which]){
                oItems[0] -> startProfileActivity(author)
                oItems[1] -> sendMessage(author)
            }
        }).setCancelable(false).show();
    }

    private fun setCommentDialog(item : CustomAdapter.AdapterItem) {

        val oItems = listOf<CharSequence>("프로필 보기", "쪽지 보내기", "대댓글 작성", "수정", "삭제").toTypedArray()
        val oDialog = AlertDialog.Builder(mContext, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
        oDialog.setTitle("무엇을 하시겠어요?").setItems(oItems, DialogInterface.OnClickListener { dialog, which ->
            when(oItems[which]){
                oItems[0] -> startProfileActivity(item.author)
                oItems[1] -> sendMessage(item.author)
                oItems[2] -> writeReply(item.commentId, userNickname)
                oItems[3] -> edit()
                oItems[4] -> delete()
            }

        }).setCancelable(false).show();
    }

    private fun sendMessage(toNickname:String){
        Toast.makeText(mContext, "send", Toast.LENGTH_LONG).show()
    }

    private fun startProfileActivity(nickname: String){
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("nickname", nickname)
        intent.putExtra("id", -1)
        startActivity (intent)
    }

    private fun writeReply(commentId: Long, nickname: String){
        val oDialog = AlertDialog.Builder(mContext, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
        oDialog.setTitle("답글 입력")
            .setMessage("GOOD")
            .setPositiveButton("입력", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Log.d("답글 입력", "입력 버튼~~")
                }
            })
            .setNegativeButton("취소", object  : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Log.d("답글 취소", "취소 버튼~~")
                }
            })
            .create()
            .show()
    }

    private fun edit(){
        Toast.makeText(mContext, "edit", Toast.LENGTH_LONG).show()

    }

    private fun delete(){
        Toast.makeText(mContext, "delete", Toast.LENGTH_LONG).show()
    }

    class CustomAdapter(private val context: PostDetail, private val items: MutableList<AdapterItem>) : BaseAdapter() {
        data class AdapterItem(
            val author:String,
            val content:String,
            var type:String,
            var commentId : Long,
            var replyId : Long
        )

        override fun getCount(): Int = items.size
        override fun getItem(position: Int): AdapterItem = items[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            if(items[position].type.equals("COMMENT"))
            {
                val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.item_comment_list, null)
                val author = view.findViewById<TextView>(R.id.userNickname_comment)
                author.text = items[position].author
                val content = view.findViewById<TextView>(R.id.contents_comment)
                content.text = items[position].content

                view.findViewById<ImageButton>(R.id.menu_btn_comment).setOnClickListener {
                    context.setCommentDialog(items[position])
                }

                return view
            }
            else
            {
                val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.item_reply_list, null)
                val author = view.findViewById<TextView>(R.id.userNickname_reply)
                author.text = items[position].author
                val content = view.findViewById<TextView>(R.id.content_reply)
                content.text = items[position].content

                view.findViewById<ImageButton>(R.id.menu_btn_reply).setOnClickListener {
                    context.setCommentDialog(items[position])
                }

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
                            items.add(CustomAdapter.AdapterItem(comment.author, comment.content, "COMMENT", comment.commentId, -1))
                            for(reply in comment.replyDtoList){
                                items.add(CustomAdapter.AdapterItem(reply.author, reply.content, "REPLY", comment.commentId, -1))
                            }
                        }
                    }

                    //        어답터 설정
                    listView.adapter = CustomAdapter(mContext, items)
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