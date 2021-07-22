package com.example.knuhack

import android.content.ClipData
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.knuhack.dto.ApiResult
import com.example.knuhack.entity.Board
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.view.menu.ListMenuItemView
import com.example.knuhack.dto.BoardForm


class postList : AppCompatActivity() {
    val boardList : ArrayList<Board> = ArrayList<Board>()
    val mContext  = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_list)
        
        intent.getStringExtra("category")?.let { getBoardList(it) }
        
        
        
       
        val items = mutableListOf<BoardForm>()

        items.add(BoardForm("카테고리1","나는 멋지다","가나다","이현섭"))
        items.add(BoardForm("카테고리2","너는 멋지다","마바사","김창묵"))
        items.add(BoardForm("카테고리3","너는 안멋지다","아자차","한현민"))
        items.add(BoardForm("카테고리4","나도 안멋지다","파타하파타하파타하파타하파타하파타하파타하파타하파타하파타하파타하파타하파타하파타하파타하파타하파타하","권현수"))


        val listView = findViewById<ListView>(R.id.listview)

//        어답터 설정
        listView.adapter = MyCustomAdapter(items)

//        Item click listener
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position) as BoardForm
            val intent = Intent(this, PostDetail::class.java)
            intent.putExtra("title",item.Title)
            intent.putExtra("author",item.author)
            intent.putExtra("content",item.content)
            intent.putExtra("category",item.category)
            startActivity(intent)
        }

    }
    private class MyCustomAdapter(private val items: MutableList<BoardForm>) : BaseAdapter() {

        override fun getCount(): Int = items.size

        override fun getItem(position: Int): BoardForm = items[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.item_post_list, null)

            val author = view.findViewById<TextView>(R.id.postAuthor)
            author.text = items[position].author
            val title = view.findViewById<TextView>(R.id.postTitle)
            title.text = items[position].Title
            val content = view.findViewById<TextView>(R.id.postContents)
            content.text = items[position].content

            return view
        }
    }
    private fun getBoardList(boardType: String){
        RestApiService.instance.findBoardByCategory(boardType).enqueue(object : Callback<ApiResult<List<Board>>>{
            override fun onResponse(call: Call<ApiResult<List<Board>>>, response: Response<ApiResult<List<Board>>>) {
                boardList.clear()

                response.body()?.let {
                    Log.i("get board list ", it.response.toString())
                    boardList.addAll(it.response)
                }
            }

            override fun onFailure(call: Call<ApiResult<List<Board>>>, t: Throwable) {
                Toast.makeText(mContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

}
