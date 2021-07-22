package com.example.knuhack

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.knuhack.dto.ApiResult
import com.example.knuhack.dto.SignInForm
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

//        val btn = findViewById<Button>(R.id.btn1) as Button
//        btn.setOnClickListener(View.OnClickListener {
//            val intent = Intent(this, Login::class.java)
//            startActivity(intent)
//        })
//        val btn2 = findViewById<Button>(R.id.btn2) as Button
//        btn2.setOnClickListener(View.OnClickListener {
//            val intent = Intent(this, SignUp::class.java)
//            startActivity(intent)
//        })


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
                    val intent = Intent(this, MyPage::class.java)
                    intent.putExtra("nickname", nickname)
                    intent.putExtra("id", id)
                    startActivity (intent)
                }

                R.id.menu_item1  -> {
                    val intent = Intent(this, postList::class.java)
                    intent.putExtra("category", "FREE")
                    intent.putExtra("nickname", nickname)
                    intent.putExtra("id", id)
                    startActivity (intent)
                }
                R.id.menu_item2 -> {
                    val intent = Intent(this, postList::class.java)
                    intent.putExtra("category", "QNA")
                    intent.putExtra("nickname", nickname)
                    intent.putExtra("id", id)
                    startActivity (intent)
                }
                /*R.id.menu_item3 -> {
                    val intent = Intent(this, postList::class.java)
                    intent.putExtra("category", "")
                    startActivity (intent)
                }*/
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

}