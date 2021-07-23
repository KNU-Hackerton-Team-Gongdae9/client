package com.example.knuhack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.example.knuhack.dto.ApiResult
import com.example.knuhack.dto.SignUpForm
import com.example.knuhack.entity.Member
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUp : AppCompatActivity(),View.OnClickListener {

    var mContext = this

    lateinit var idEdit : EditText
    lateinit var idCheck : Button
    lateinit var pw : EditText
    lateinit var pwConfirm : EditText
    lateinit var name : EditText
    lateinit var nickName : EditText
    lateinit var studentId : EditText
    lateinit var grade : EditText
    lateinit var majorComputer : RadioButton
    lateinit var majorGlobal : RadioButton

    lateinit var signUpButoon : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initView()
        setUpListener()
    }

    fun initView(){
        idEdit=findViewById<EditText>(R.id.input_id) as EditText
        pw=findViewById<EditText>(R.id.input_pw) as EditText
        pwConfirm=findViewById<EditText>(R.id.input_pw_confirm) as EditText
        name=findViewById<EditText>(R.id.input_name) as EditText
        nickName=findViewById<EditText>(R.id.input_nickname) as EditText
        studentId=findViewById<EditText>(R.id.input_student_number) as EditText
        grade=findViewById<EditText>(R.id.input_grade) as EditText
        majorComputer=findViewById<RadioButton>(R.id.computer) as RadioButton
        majorGlobal=findViewById<RadioButton>(R.id.global) as RadioButton

        signUpButoon=findViewById<Button>(R.id.join_submit) as Button


    }

    fun setUpListener(){
        signUpButoon!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val id = v.id

        when (id){
            R.id.join_submit ->{
                requestJoin()
            }
        }

    }

    fun requestJoin() {
        if (idEdit!!.text.toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "이메일을 입력하세요.", Toast.LENGTH_SHORT).show()
            idEdit!!.requestFocus()
            return
        }
        if (pw!!.text.toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            pw!!.requestFocus()
            return
        }
        if (pwConfirm!!.text.toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "비밀번호 확인을 입력하세요.", Toast.LENGTH_SHORT).show()
            pwConfirm!!.requestFocus()
            return
        }
        if (name!!.text.toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "이름을 입력하세요.", Toast.LENGTH_SHORT).show()
            name!!.requestFocus()
            return
        }
        if (nickName!!.text.toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show()
            nickName!!.requestFocus()
            return
        }
        val isCheckPw = pwPattern(pw!!.text.toString())
        if (!isCheckPw) {
            Toast.makeText(getApplicationContext(), "비밀번호가 형식에 맞지 않습니다.", Toast.LENGTH_SHORT).show()
            pw!!.requestFocus()
            return
        }
        if (pw!!.text.toString() != pwConfirm!!.text.toString()) {
            Toast.makeText(getApplicationContext(), "비밀번호와 비밀번호 확인이 다릅니다.", Toast.LENGTH_SHORT)
                .show()
            pwConfirm!!.requestFocus()
            return
        }
        var isCheckMajor:Boolean=false
        var majorString:String=""

        if(majorGlobal.isChecked){
            majorString="ADVANCED"
            isCheckMajor=true
        }
        else if(majorComputer.isChecked){
            majorString="GLOBAL"
            isCheckMajor=true
        }

        if(!isCheckMajor) return


        val member = SignUpForm(idEdit.text.toString(),Integer.parseInt(grade.text.toString()),
             majorString,nickName.text.toString(),pw.text.toString(),studentId.text.toString(),name.text.toString());


        RestApiService.instance.signUp(member).enqueue(object : Callback<ApiResult<String>> {
            override fun onResponse(call: Call<ApiResult<String>>, response: Response<ApiResult<String>>) {
                response.body()?.let{
                    if(!it.success){
                        Toast.makeText(mContext, it.error.message, Toast.LENGTH_SHORT).show()

                    }

                    it.response?.let {
                        Toast.makeText(mContext, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show()

                        intent = Intent(mContext, Login::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<ApiResult<String>>, t: Throwable) {
                Toast.makeText(mContext,"회원가입에 실패 하였습니다.", Toast.LENGTH_SHORT).show()
                t.message?.let { Log.e("login failed", it) }
            }
        })

    }

    // 이메일 패턴 검사
    fun emailPattern(email: String): Boolean {
        val repExp =
            Regex("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")
        return email.matches(repExp)
    }

    // 비밀번호 패턴 검사
    fun pwPattern(pw: String): Boolean {
        val repExp = Regex("^([0-9a-zA-Z]).{3,20}$")
        return pw.matches(repExp)
    }
}


