package com.example.knuhack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.knuhack.dto.ApiResult
import com.example.knuhack.entity.Board
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.knuhack.dto.BoardForm


class postList : AppCompatActivity() {
    val items : ArrayList<Board> = ArrayList<Board>()
    val mContext  = this
    lateinit var listView: ListView
    var userId : Long = -1
    lateinit var userNickname : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_list)

        intent.getStringExtra("nickname")?.let { userNickname = it }
        userId = intent.getLongExtra("userId", -1)
        intent.getStringExtra("category")?.let { getBoardList(it) }
        listView = findViewById<ListView>(R.id.listview)

//        val btn = findViewById<Button>(R.id.writebtn) as Button
//        btn.setOnClickListener(View.OnClickListener {
//            val intent = Intent(this, write_post::class.java)
//            intent.putExtra("nickname", userNickname)
//            startActivity(intent)
//            val adapter = MyCustomAdapter(items)
//            adapter.notifyDataSetChanged()
//        })

//        Item click listener
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position) as Board
            val intent = Intent(this, PostDetail::class.java)
            intent.putExtra("boardId", item.boardId)
            intent.putExtra("title",item.title)
            intent.putExtra("author",item.author)
            intent.putExtra("content",item.content)
            intent.putExtra("category",item.category)
            intent.putExtra("userId", userId)
            intent.putExtra("nickname", userNickname)

            startActivity(intent)
        }


    }
    private class MyCustomAdapter(private val items: MutableList<Board>) : BaseAdapter() {

        override fun getCount(): Int = items.size

        override fun getItem(position: Int): Board = items[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.item_post_list, null)

            val author = view.findViewById<TextView>(R.id.postAuthor)
            author.text = items[position].author
            val title = view.findViewById<TextView>(R.id.postTitle)
            title.text = items[position].title
            val content = view.findViewById<TextView>(R.id.postContents)
            content.text = items[position].content

            return view
        }
    }
    private fun getBoardList(boardType: String){
        RestApiService.instance.findBoardByCategory(boardType).enqueue(object : Callback<ApiResult<List<Board>>>{
            override fun onResponse(call: Call<ApiResult<List<Board>>>, response: Response<ApiResult<List<Board>>>) {
                items.clear()

                response.body()?.response?.let{
                    items.addAll(it)
                    //        어답터 설정
                    listView.adapter = MyCustomAdapter(items)
                }
            }

            override fun onFailure(call: Call<ApiResult<List<Board>>>, t: Throwable) {
                Toast.makeText(mContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

}
