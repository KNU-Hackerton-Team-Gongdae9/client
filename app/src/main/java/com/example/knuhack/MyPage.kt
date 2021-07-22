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

class MyPage : AppCompatActivity() {
    lateinit var myProfile : Profile
    val mContext  = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
    }


    private fun getMyProfile(nickname : String){
        RestApiService.instance.getProfile(nickname).enqueue(object : Callback<ApiResult<Profile>>{
            override fun onResponse(call : Call<ApiResult<Profile>>, response: Response<ApiResult<Profile>>){
                response.body()?.let{
                    Log.i("get my profile", it.response.toString())
                    myProfile = it.response
                }
            }

            override fun onFailure(call: Call<ApiResult<Profile>>, t: Throwable) {
                Toast.makeText(mContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
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