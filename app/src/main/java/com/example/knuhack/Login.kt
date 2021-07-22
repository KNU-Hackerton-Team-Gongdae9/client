package com.example.knuhack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.knuhack.dto.ApiResult
import com.example.knuhack.dto.SignInForm
import com.example.knuhack.entity.Board
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    var mContext = this
    lateinit var idEditText : EditText
    lateinit var pwEditText: EditText
    lateinit var loginBtn : Button
    lateinit var signUpBtn : Button
    lateinit var findIdBtn : Button
    lateinit var findPwBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView()
    }

    fun initView(){
        idEditText = findViewById<EditText>(R.id.input_id) as EditText
        pwEditText = findViewById<EditText>(R.id.input_pw) as EditText
        loginBtn = findViewById<Button>(R.id.login_btn) as Button

        signUpBtn = findViewById<Button>(R.id.join_btn) as Button
        findIdBtn = findViewById<Button>(R.id.find_id) as Button
        findPwBtn = findViewById<Button>(R.id.find_pw) as Button


        loginBtn.setOnClickListener {
            val id = idEditText.text.toString().trim{ it <= ' '}
            val pw = pwEditText.text.toString().trim{ it <= ' ' }
            requestLogin(id, pw)
        }
    }

    fun requestLogin(id: String, pw: String) {
        RestApiService.instance.signin(SignInForm(id,pw)).enqueue(object : Callback<ApiResult<String>> {
            override fun onResponse(call: Call<ApiResult<String>>, response: Response<ApiResult<String>>) {
                //내가 할거
                RestApiService.instance.getUserId().enqueue(object : Callback<Long>{
                    override fun onResponse(call: Call<Long>, response: Response<Long>) {
                        Toast.makeText(mContext,"로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                        response.body()?.let { Log.i("get user id", it.toString()) }

                        intent = Intent(mContext, MainActivity::class.java)
                        response.body()?.let { intent.getLongExtra("userId", it) }
                        startActivity(intent)
                        finish()
                    }

                    override fun onFailure(call: Call<Long>, t: Throwable) {
                        t.message?.let { Log.e("failed get user id", it) }
                    }
                })
            }

            override fun onFailure(call: Call<ApiResult<String>>, t: Throwable) {
                Toast.makeText(mContext,"로그인에 x하였습니다.", Toast.LENGTH_SHORT).show()
                t.message?.let { Log.e("login failed", it) }
            }
        })
    }
}