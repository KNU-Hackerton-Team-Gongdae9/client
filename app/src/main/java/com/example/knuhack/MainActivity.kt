package com.example.knuhack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.knuhack.dto.ApiResult
import com.example.knuhack.entity.Board
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    lateinit var toggle : ActionBarDrawerToggle

    val global_items : ArrayList<Board> = ArrayList<Board>()
    val mContext  = this
    lateinit var listView: ListView
    lateinit var  drawerLayout : DrawerLayout
    lateinit var navigationView: NavigationView
    var findcategory = "FREE"


    lateinit var nickname: String
    var id : Long = 0
    lateinit var adapter : MyCustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        id = intent.getLongExtra("id", -1)
        intent.getStringExtra("nickname")?.let { nickname = it }

        adapter=MyCustomAdapter(this, global_items)

        listView = findViewById<ListView>(R.id.mainList)
        listView.adapter=adapter
        getBoardList(findcategory)

        val image = findViewById<ImageView>(R.id.imageView) as ImageView
        image.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext, // Context
                R.drawable.free_board//drawable
            ))
    //    findcategory?.let { getBoardList(it) }
        //메인 리스트

        val btn = findViewById<TextView>(R.id.textView_freeBoard) as TextView
        btn.setOnClickListener(View.OnClickListener {
            image.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext, // Context
                    R.drawable.free_board//drawable
                ))
            findcategory = "FREE"
            getBoardList(findcategory)

        })//QNA,FREE,TEAM
        val btn2 = findViewById<TextView>(R.id.textView_QnA) as TextView
        btn2.setOnClickListener(View.OnClickListener {
            image.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext, // Context
                    R.drawable.qna//drawable
                ))
            findcategory = "QNA"
            getBoardList(findcategory)
        })
        val btn3 = findViewById<TextView>(R.id.textView_team) as TextView
        btn3.setOnClickListener(View.OnClickListener {
            image.setImageDrawable(
                ContextCompat.getDrawable(
                applicationContext, // Context
                R.drawable.team_project//drawable
            ))
            findcategory = "TEAM"
            getBoardList(findcategory)
        })
        val btn4 = findViewById<TextView>(R.id.texView_study) as TextView
        btn4.setOnClickListener(View.OnClickListener {
            image.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext, // Context
                    R.drawable.study//drawable
                ))
            findcategory = "STUDY"
            getBoardList(findcategory)
        })



        drawerLayout  = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.menu_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.putExtra("nickname", nickname)
                    intent.putExtra("id", id)
                    startActivity (intent)
                }

                R.id.menu_item1  -> {
                    val intent = Intent(this, postList::class.java)
                    intent.putExtra("category", "FREE")
                    intent.putExtra("nickname", nickname)
                    intent.putExtra("userId", id)
                    startActivity (intent)
                }
                R.id.menu_item2 -> {
                    val intent = Intent(this, postList::class.java)
                    intent.putExtra("category", "QNA")
                    intent.putExtra("nickname", nickname)
                    intent.putExtra("userId", id)
                    startActivity (intent)
                }
                R.id.menu_item3 -> {
                    val intent = Intent(this, postList::class.java)
                    intent.putExtra("category", "TEAM")
                    intent.putExtra("nickname", nickname)
                    intent.putExtra("userId", id)
                    startActivity (intent)
                }
            }
            true
        }


//        intent = Intent(mContext, postList::class.java)
//        intent.putExtra("category", "FREE")
//        startActivity(intent)
//        finish()




    }

    fun goBoardDetailActivity(item : Board){
            val intent = Intent(this, PostDetail::class.java)
            intent.putExtra("boardId", item.boardId)
            intent.putExtra("title",item.title)
            intent.putExtra("author",item.author)
            intent.putExtra("content",item.content)
            intent.putExtra("category",item.category)

            intent.putExtra("userId", id)
            intent.putExtra("nickname", nickname)

        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            true
        }
        return super.onOptionsItemSelected(item)
    }
    class MyCustomAdapter(val mainActivity : MainActivity, private val items: ArrayList<Board>) : BaseAdapter() {

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

            view.setOnClickListener {
                mainActivity.goBoardDetailActivity(items[position])
            }

            return view
        }
        fun changeItems(newitems: ArrayList<Board>) {

            items.clear()
            Log.e("asd2213f",newitems.toString())
            items.addAll(newitems)
            Log.e("asdf",items.toString())
            notifyDataSetChanged()

        }

    }
    private fun getBoardList(boardType: String){
        RestApiService.instance.findBoardByCategory(boardType).enqueue(object : Callback<ApiResult<List<Board>>> {
            override fun onResponse(call: Call<ApiResult<List<Board>>>, response: Response<ApiResult<List<Board>>>) {
                if(response.body()!=null && response.body()!!.response != null){
                    adapter.changeItems(response.body()!!.response as ArrayList<Board>)
                }
                else
                {
                    adapter.changeItems(ArrayList<Board>())
                }
            }

            override fun onFailure(call: Call<ApiResult<List<Board>>>, t: Throwable) {
                Toast.makeText(mContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}