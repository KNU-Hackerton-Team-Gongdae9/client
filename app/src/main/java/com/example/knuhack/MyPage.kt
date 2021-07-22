package com.example.knuhack

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
import com.bumptech.glide.Glide


class MyPage : AppCompatActivity() {
    val mContext  = this

    // data
    private lateinit var myProfile : Profile
    private lateinit var nickname : String
    private var id : Long = 0

    // view
    private lateinit var profileImageView: ImageView

    private lateinit var nicknameTextView: TextView
    private lateinit var langTextView: TextView
    private lateinit var interestTextView: TextView

    private lateinit var githubImageView : ImageView
    private lateinit var velogImageView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        initView()


        id = intent.getLongExtra("id", -1)
        intent.getStringExtra("nickname")?.let {
            nickname = it
            getMyProfile(nickname)
        }
    }

    private fun initView(){
        profileImageView = findViewById<ImageView>(R.id.profile_image)

        nicknameTextView = findViewById<TextView>(R.id.profile_nickname)
        langTextView = findViewById<TextView>(R.id.profile_language)
        interestTextView = findViewById<TextView>(R.id.profile_interest)

        githubImageView = findViewById<ImageView>(R.id.profile_github)
        velogImageView = findViewById<ImageView>(R.id.profile_velog)
    }

    private fun getMyProfile(nickname : String){
        RestApiService.instance.getProfile(nickname).enqueue(object : Callback<ApiResult<Profile>>{
            override fun onResponse(call : Call<ApiResult<Profile>>, response: Response<ApiResult<Profile>>){
                response.body()?.let{
                    Log.i("get my profile", it.response.toString())
                    myProfile = it.response
                    setWithProfile()
                }
            }

            override fun onFailure(call: Call<ApiResult<Profile>>, t: Throwable) {
                Toast.makeText(mContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setWithProfile(){
        myProfile.let{ profile ->
            nicknameTextView.text = nickname
            langTextView.text = profile.language
            interestTextView.text = profile.interset

            if(profile.githubLink.startsWith("http")) {
                githubImageView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(profile.githubLink))
                    startActivity(intent)
                }
            }

            if(profile.blogLink.startsWith("http")) {
                velogImageView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(profile.blogLink))
                    startActivity(intent)
                }
            }

            if(profile.imageLink.startsWith("http")){
                Glide.with(mContext).load(profile.imageLink).into(profileImageView);
            }
        }
    }

    private fun createMyProfile(member_id : Long, language : String, interest : String, githubLink : String, blogLink : String, imageLink : String){
        RestApiService.instance.createProfile(member_id, ProfileForm(language, interest, githubLink, blogLink, imageLink)).enqueue(object : Callback<ApiResult<String>>{
            override fun onResponse(call : Call<ApiResult<String>>, response: Response<ApiResult<String>>) {
                Toast.makeText(mContext, "프로필을 성공적으로 생성하였습니다.", Toast.LENGTH_SHORT).show()
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