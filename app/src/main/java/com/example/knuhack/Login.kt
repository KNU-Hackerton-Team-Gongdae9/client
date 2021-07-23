package com.example.knuhack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.knuhack.dto.ApiResult
import com.example.knuhack.dto.SignInForm
import com.example.knuhack.entity.Member
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

        signUpBtn.setOnClickListener{
            var i = Intent(getApplicationContext(), SignUp::class.java)
            startActivity(i)
        }
    }

    fun requestLogin(id: String, pw: String) {
        RestApiService.instance.signIn(SignInForm(id,pw)).enqueue(object : Callback<ApiResult<Member>> {
            override fun onResponse(call: Call<ApiResult<Member>>, response: Response<ApiResult<Member>>) {
                response.body()?.let{
                    if(!it.success){
                        Toast.makeText(mContext, it.error.message, Toast.LENGTH_SHORT).show()

                    }

                    it.response?.let {
                        Toast.makeText(mContext, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()

                        intent = Intent(mContext, MainActivity::class.java)
                        intent.putExtra("id", it.userId)
                        intent.putExtra("nickname", it.nickname)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<ApiResult<Member>>, t: Throwable) {
                Toast.makeText(mContext,"로그인에 x하였습니다.", Toast.LENGTH_SHORT).show()
                t.message?.let { Log.e("login failed", it) }
            }
        })
    }
}