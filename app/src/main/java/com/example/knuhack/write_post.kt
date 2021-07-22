package com.example.knuhack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.knuhack.dto.ApiResult
import com.example.knuhack.dto.BoardForm
import com.example.knuhack.entity.Board
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_write_post.*

class write_post : AppCompatActivity(), ItemClickListener {
    val mContext = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_post)
        val requestBtn = findViewById<Button>(R.id.message_button) as Button

        val titleText = findViewById<EditText>(R.id.input_id) as EditText
        val contentText = findViewById<EditText>(R.id.message_input) as EditText
        val slect_board = findViewById<Button>(R.id.slect_board) as Button
        val nickname = intent.getStringExtra("nickname")

        requestBtn.setOnClickListener {
            val title = titleText.text.toString().trim{ it <= ' '}
            val content = contentText.text.toString().trim{ it <= ' '}
            var category = slect_board.text.toString().trim{ it <= ' '}

            /* 카테고리 ENUM 타입으로 변경 */
            if(category.equals("Q&A")) category = "QNA"
            else if(category.equals("자유게시판")) category = "FREE"
            else if(category.equals("팀프로젝트")) category = "TEAM"

            Log.e("category 값 : ", category)
            if (nickname != null) {
                writeBoardRequest(nickname, category, title, content)
            }
        }

/*        val btn = findViewById<Button>(R.id.slect_board) as Button
        btn.setOnClickListener {
            openBottomSheet()
        }*/
        slect_board.setOnClickListener { openBottomSheet() }
    }

    fun writeBoardRequest(nickname: String, category: String, title: String, content: String) {

        RestApiService.instance.writeBoard(BoardForm(category, title, content, nickname)).enqueue(object : Callback<ApiResult<Board>> {
            override fun onResponse(call: Call<ApiResult<Board>>, response: Response<ApiResult<Board>>) {
                response.body()?.let {
                    Log.i("게시물 작성이 성공적으로 수행되었습니다.", it.toString())

                }
            }

            override fun onFailure(call: Call<ApiResult<Board>>, t: Throwable) {
                Toast.makeText(mContext, "게시물 작성에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                t.message?.let { Log.e("comment writing failed", it) }
            }
        })
    }

    fun openBottomSheet(){
        val addPhotoBottomDialogFragment = ActionBottom.newInstance()
        addPhotoBottomDialogFragment.show(
            supportFragmentManager,ActionBottom.TAG
        )
    }

    override fun onItemClick(item: String?) {
        slect_board.text = "$item"
    }
}
