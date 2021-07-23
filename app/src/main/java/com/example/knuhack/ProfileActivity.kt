package com.example.knuhack

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.knuhack.dto.ApiResult
import com.example.knuhack.entity.Profile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.*
import com.example.knuhack.dto.ProfileForm
import android.content.Intent
import android.net.Uri
import android.view.View
import com.bumptech.glide.Glide
import com.example.knuhack.dto.ReplyForm
import com.example.knuhack.entity.Reply


class ProfileActivity : AppCompatActivity() {
    val mContext  = this

    // data
    private lateinit var profile : Profile
    private lateinit var nickname : String
    private var id : Long = -1

    // view
    private lateinit var profileImageView: ImageView

    private lateinit var nicknameTextView: TextView
    private lateinit var skillTextView: TextView
    private lateinit var majorTextView: TextView
    private lateinit var emailTextView : TextView
    private lateinit var gradeTextView: TextView

    private lateinit var githubImageView : ImageView
    private lateinit var velogImageView : ImageView

    private lateinit var changeButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        id = intent.getLongExtra("userId", -1)
        intent.getStringExtra("nickname")?.let {
            nickname = it
            initView()
            getMyProfile(nickname)
        }
    }

    private fun initView(){
        profileImageView = findViewById<ImageView>(R.id.profile_image)

        nicknameTextView = findViewById<TextView>(R.id.profile_nickname)
        skillTextView = findViewById<TextView>(R.id.profile_Skill)
        majorTextView = findViewById<TextView>(R.id.profile_major)
        emailTextView = findViewById<TextView>(R.id.profile_Email)
        gradeTextView = findViewById<TextView>(R.id.profile_grade)

        githubImageView = findViewById<ImageView>(R.id.profile_github)
        velogImageView = findViewById<ImageView>(R.id.profile_velog)

        changeButton = findViewById<ImageButton>(R.id.menu_btn_profile)

        if(id == -1L) changeButton.visibility = View.INVISIBLE
        else changeButton.visibility = View.VISIBLE

        changeButton.setOnClickListener {
            setChangeDialog()
        }
    }

    private fun getMyProfile(nickname : String){
        RestApiService.instance.getProfile(nickname).enqueue(object : Callback<ApiResult<Profile>>{
            override fun onResponse(call : Call<ApiResult<Profile>>, response: Response<ApiResult<Profile>>){
                if(response.body()!= null && response.body()!!.response != null){
                    profile = response.body()!!.response
                    setWithProfile()
                }
            }

            override fun onFailure(call: Call<ApiResult<Profile>>, t: Throwable) {
                Toast.makeText(mContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setWithProfile(){
        nicknameTextView.text = nickname

        profile.language?.let { skillTextView.text = it }
        profile.mager?.let { majorTextView.text = it }
        profile.email?.let { emailTextView.text = it }
        profile.grade?.let { gradeTextView.text = it.toString() }



        profile.githubLink?.let{
            if(it.startsWith("http")) {
                githubImageView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(profile.githubLink))
                    startActivity(intent)
                }
            }
        }

        profile.blogLink?.let{
            if(it.startsWith("http")) {
                velogImageView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(profile.blogLink))
                    startActivity(intent)
                }
            }
        }


        profile.imageLink?.let{
            if(it.startsWith("http")){
                Glide.with(mContext).load(profile.imageLink).into(profileImageView);
            }
        }
    }

//    private fun createMyProfile(member_id : Long, language : String, interest : String, githubLink : String, blogLink : String, imageLink : String){
//        RestApiService.instance.createProfile(member_id, ProfileForm(language, interest, githubLink, blogLink, imageLink)).enqueue(object : Callback<ApiResult<String>>{
//            override fun onResponse(call : Call<ApiResult<String>>, response: Response<ApiResult<String>>) {
//                Toast.makeText(mContext, "프로필을 성공적으로 생성하였습니다.", Toast.LENGTH_SHORT).show()
//                response.body()?.let{
//                    Log.i("profile created", it.response)
//                }
//            }
//
//            override fun onFailure(call: Call<ApiResult<String>>, t: Throwable) {
//                Toast.makeText(mContext, t.message, Toast.LENGTH_SHORT).show()
//            }
//        })
//    }

    private fun setChangeDialog(){
        Toast.makeText(mContext, "edit", Toast.LENGTH_LONG).show()

        val dialogView = layoutInflater.inflate(R.layout.dialog_profile_change, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()


        val nicknameEdit =  dialogView.findViewById<EditText>(R.id.profile_update_nickname)
        nicknameEdit.setText(nickname)

        val emailEdit =dialogView.findViewById<EditText>(R.id.profile_update_email)
        profile.email?.let { emailEdit.setText(it) }

        val gradeEdit =dialogView.findViewById<EditText>(R.id.profile_update_grade)
        profile.grade?.let { gradeEdit.setText(it.toString())}

        val langEdit =  dialogView.findViewById<EditText>(R.id.profile_update_skill)
        profile.language?.let { langEdit.setText(it) }

        val blogEdit = dialogView.findViewById<EditText>(R.id.profile_update_blog_link)
        profile.blogLink?.let { blogEdit.setText(it) }

        val githubEdit = dialogView.findViewById<EditText>(R.id.profile_update_github_link)
        profile.githubLink?.let { githubEdit.setText(it) }

        val confirmBtn = dialogView.findViewById<Button>(R.id.profile_update_confirm_btn)
        val cancelBtn = dialogView.findViewById<Button>(R.id.profile_update_cancle_btn)

        confirmBtn.setOnClickListener {
            alertDialog.dismiss()
            nickname = nicknameEdit.text.toString()
            profile.email = emailEdit.text.toString()
            profile.grade = Integer.parseInt(gradeEdit.text.toString())
            profile.language = langEdit.text.toString()
            profile.blogLink = blogEdit.text.toString()
            profile.githubLink = githubEdit.text.toString()

            changeMyProfile(ProfileForm(profile.email, profile.grade, nickname, profile.language, profile.githubLink, profile.blogLink))
        }

        cancelBtn.setOnClickListener {
            alertDialog.dismiss()
            Log.d("수정 취소 ", "수정을 취소했습니다.")
        }

        alertDialog.show()
    }


    private fun changeMyProfile(profileForm: ProfileForm){
        RestApiService.instance.changeProfile(id, profileForm).enqueue(object : Callback<ApiResult<String>>{
            override fun onResponse(call : Call<ApiResult<String>>, response: Response<ApiResult<String>>) {
                Toast.makeText(mContext, "프로필을 성공적으로 수정하였습다.", Toast.LENGTH_SHORT).show()
                response.body()?.let{
                    Log.i("profile created", it.response)
                }
            }

            override fun onFailure(call: Call<ApiResult<String>>, t: Throwable) {
                Toast.makeText(mContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}