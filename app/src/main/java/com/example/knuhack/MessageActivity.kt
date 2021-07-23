package com.example.knuhack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.knuhack.dto.ApiResult
import com.example.knuhack.entity.Message
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageActivity : AppCompatActivity() {
    // data
    val items : ArrayList<Message> = ArrayList<Message>()
    var userId : Long = -1
    lateinit var userNickname : String

    val mContext  = this
    lateinit var listView: ListView
    //사이드바
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var  drawerLayout : DrawerLayout
    lateinit var navigationView: NavigationView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actvity_all_message_main)

        //사이드바
        drawerLayout  = findViewById(R.id.message_drawerLayout)
        navigationView = findViewById(R.id.message_nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.menu_message -> {
                    val intent = Intent(this, MessageActivity::class.java)
                    intent.putExtra("nickname", userNickname)
                    intent.putExtra("userId", userId)
                    startActivity (intent)
                }

                R.id.menu_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.putExtra("nickname", userNickname)
                    intent.putExtra("id", userId)
                    startActivity (intent)
                }

                R.id.menu_home  -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("category", "FREE")
                    intent.putExtra("nickname", userNickname)
                    intent.putExtra("userId", userId)
                    startActivity (intent)
                }

            }
            true
        }

        intent.getStringExtra("nickname")?.let { userNickname = it }
        userId = intent.getLongExtra("userId", -1)
        listView = findViewById<ListView>(R.id.message_list)
        getMessageList()
    }

    private class MyCustomAdapter(private val items: MutableList<Message>) : BaseAdapter() {

        override fun getCount(): Int = items.size

        override fun getItem(position: Int): Message = items[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.item_message_list_main, null)

            view.findViewById<TextView>(R.id.message_userName).text = items[position].otherNickname
            view.findViewById<TextView>(R.id.message_date_mainPage).text = items[position].time
            view.findViewById<TextView>(R.id.message_content_mainPage).text = items[position].content

            return view
        }

    }
    private fun getMessageList(){
        RestApiService.instance.getReceived(userId).enqueue(object : Callback<ApiResult<List<Message>>> {
            override fun onResponse(call: Call<ApiResult<List<Message>>>, response: Response<ApiResult<List<Message>>>) {
                items.clear()
                response.body()?.response?.let{
                    items.addAll(it)
                    //        어답터 설정
                    listView.adapter = MyCustomAdapter(items)

                    listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                        val item = parent.getItemAtPosition(position) as Message
                        val intent = Intent(mContext, MessageOneActivity::class.java)

                        intent.putExtra("content",item.content)
                        intent.putExtra("time", item.time)
                        intent.putExtra("otherNickname", item.otherNickname)
                        intent.putExtra("userId", userId)
                        intent.putExtra("nickname", userNickname)

                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<ApiResult<List<Message>>>, t: Throwable) {
                Toast.makeText(mContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}