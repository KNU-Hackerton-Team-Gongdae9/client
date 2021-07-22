package com.example.knuhack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.knuhack.dto.CommentForm
import com.example.knuhack.entity.Reply


class PostDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

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

        items.add(test(1,"나는 멋지다","가나다"))//1이면 댓글 2이면 대댓글
        items.add(test(2,"as","가나다"))
        items.add(test(2,"fasd","가나다"))

        items.add(test(2,"asqwd","마바사"))
        items.add(test(1,"너는 안멋지다","아자차"))
        items.add(test(1,"나도 안멋지다","파타파타하"))


        val commentlistview = findViewById<ListView>(R.id.commentlistview)

        commentlistview.adapter = CustomAdapter(items)

    }

    private class CustomAdapter(private val items: MutableList<test>) : BaseAdapter() {

        override fun getCount(): Int = items.size
        override fun getItem(position: Int): test = items[position]


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
    private class ReCustomAdapter(private val items: MutableList<Reply>) : BaseAdapter() {

        override fun getCount(): Int = items.size
        override fun getItem(position: Int): Reply = items[position]


        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.item_reply_list, null)
            val author = view.findViewById<TextView>(R.id.userNickname)
            author.text = items[position].author
            val content = view.findViewById<TextView>(R.id.contents)
            content.text = items[position].content
            return view
        }
    }
}