package com.example.knuhack

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.example.knuhack.dto.ApiResult
import com.example.knuhack.dto.SignUpForm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUp : AppCompatActivity(),View.OnClickListener {

    var mContext = this

    lateinit var idEdit : EditText
    lateinit var idCheckView : TextView
    lateinit var pw : EditText
    lateinit var pwConfirm : EditText
    lateinit var name : EditText
    lateinit var nickName : EditText
    lateinit var studentId : EditText
    lateinit var grade : EditText

    lateinit var radioGroup : RadioGroup
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
        idCheckView=findViewById<TextView>(R.id.input_id_check) as TextView
        pwConfirm=findViewById<EditText>(R.id.input_pw_confirm) as EditText
        name=findViewById<EditText>(R.id.input_name) as EditText
        nickName=findViewById<EditText>(R.id.input_nickname) as EditText
        studentId=findViewById<EditText>(R.id.input_student_number) as EditText
        grade=findViewById<EditText>(R.id.input_grade) as EditText
        radioGroup=findViewById<RadioGroup>(R.id.radioGroup1) as RadioGroup
        majorComputer=findViewById<RadioButton>(R.id.radio1) as RadioButton
        majorGlobal=findViewById<RadioButton>(R.id.radio0) as RadioButton
        signUpButoon=findViewById<Button>(R.id.join_submit) as Button


    }

    fun setUpListener(){
        signUpButoon.setOnClickListener(this)
        idEdit.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                idCheckView!!.visibility = View.VISIBLE
                val isCheckEmail = emailPattern(idEdit.text.toString())
                val isCheckKnu = knuPattern(idEdit.text.toString())

                if (isCheckEmail && isCheckKnu) {
                    idCheckView.text = "적합한 형식의 아이디(이메일) 입니다."
                    idCheckView.setTextColor(Color.parseColor("#6e6e6e"))
                } else if(isCheckEmail && !isCheckKnu){
                    idCheckView.text = "knu.ac.kr로 가입가능합니다"
                   idCheckView.setTextColor(Color.parseColor("#df504a"))
                }
                else if(!isCheckEmail){
                    idCheckView.text = "이메일 형식에 맞지 않습니다."
                    idCheckView.setTextColor(Color.parseColor("#df504a"))
                }
            }
        })
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

        var majorString:String=""


        if(majorComputer.isChecked) {
            majorString="ADVANCED"
        }
        else if(majorGlobal.isChecked){
            majorString="GLOBAL"
        }
        else
            return

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

    // knu 이메일 패턴 검사
    fun knuPattern(email: String): Boolean {
        val emailAddress = email.substring(email.indexOf("@") + 1);
        return emailAddress.equals("knu.ac.kr")
    }

    // 비밀번호 패턴 검사
    fun pwPattern(pw: String): Boolean {
        val repExp = Regex("^([0-9a-zA-Z]).{3,20}$")
        return pw.matches(repExp)
    }
}


