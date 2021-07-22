package com.example.knuhack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    lateinit var toggle : ActionBarDrawerToggle

    lateinit var  drawerLayout : DrawerLayout
    lateinit var navigationView: NavigationView

    lateinit var nickname: String
    var id : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        id = intent.getLongExtra("id", -1)
        intent.getStringExtra("nickname")?.let { nickname = it }

//        nickname = intent.getStringExtra("nickname").toString()
//        id = intent.getLongExtra("id", -1)

        val image = findViewById<ImageView>(R.id.imageView) as ImageView
        image.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext, // Context
                R.drawable.free_board//drawable
            ))
        val btn = findViewById<TextView>(R.id.textView_freeBoard) as TextView
        btn.setOnClickListener(View.OnClickListener {
            image.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext, // Context
                    R.drawable.free_board//drawable
                ))
        })
        val btn2 = findViewById<TextView>(R.id.textView_QnA) as TextView
        btn2.setOnClickListener(View.OnClickListener {
            image.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext, // Context
                    R.drawable.qna//drawable
                ))
        })
        val btn3 = findViewById<TextView>(R.id.textView_team) as TextView
        btn3.setOnClickListener(View.OnClickListener {
            image.setImageDrawable(
                ContextCompat.getDrawable(
                applicationContext, // Context
                R.drawable.team_project//drawable
            ))
        })
        val btn4 = findViewById<TextView>(R.id.texView_study) as TextView
        btn4.setOnClickListener(View.OnClickListener {
            image.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext, // Context
                    R.drawable.study//drawable
                ))
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
                R.id.menu_message -> {
                    val intent = Intent(this, MessageActivity::class.java)
                    intent.putExtra("nickname", nickname)
                    intent.putExtra("userId", id)
                    startActivity (intent)
                }

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            true
        }
        return super.onOptionsItemSelected(item)
    }
//
//    private fun getBoardList(boardType: String){
//        RestApiService.instance.findBoardByCategory(boardType).enqueue(object : Callback<ApiResult<List<Board>>>{
//            override fun onResponse(call: Call<ApiResult<List<Board>>>, response: Response<ApiResult<List<Board>>>) {
//                items.clear()
//
//                response.body()?.response?.let{
//                    items.addAll(it)
//                    //        어답터 설정
//                    listView.adapter = MyCustomAdapter(items)
//                }
//            }
//
//            override fun onFailure(call: Call<ApiResult<List<Board>>>, t: Throwable) {
//                Toast.makeText(mContext, t.message, Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
}