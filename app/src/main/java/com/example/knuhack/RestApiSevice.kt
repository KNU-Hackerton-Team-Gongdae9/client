package com.example.knuhack


import android.provider.ContactsContract
import com.example.knuhack.dto.*
import com.example.knuhack.entity.*
import retrofit2.Call
import retrofit2.http.*

interface RestApiService {
    @GET("/api/borad/{boardId}")fun getboard(@Path("boardId")boardId : Long) : Call<ApiResult<Board>>
    @GET("/api/board/findAuthor")fun findBoardByAuthor(@Query("author")author:String) : Call<ApiResult<List<Board>>>
    @GET("/api/board/findCategory")fun findBoardByCategory(@Query("category")category:String) : Call<ApiResult<List<Board>>>
    @GET("/api/board/findTitle")fun findBoardByTitle(@Query("title")title:String) :  Call<ApiResult<List<Board>>>
    @POST("/api/board/write")fun writeBoard(@Body boardForm:BoardForm) : Call<ApiResult<Board>>

    @POST("/api/comment/write/{boardId}")fun writeComment(@Path("boardId")boardId:Long,@Body commentForm: CommentForm) : Call<ApiResult<Comment>>
    @GET("/api/comment/findContentsByBoardId")fun findContentsByBoardId(@Query("boardId")boardId: Long) : Call<ApiResult<List<Comment>>>

    @POST("/message/from/{sender_id}/to/{receiver_nickname}")fun send(@Body messageForm: MessageForm, @Path("sender_id")sender_id:Long, @Path("receiver_nickname") receiver_nickname:String) : Call<ApiResult<String>>
    @GET("/message/user/{user_nickname}/sender/{other_nickname}")fun getChatList(@Path("user_nickname")myId:String, @Path("other_nickname")otherNickname: String) : Call<ApiResult<List<Message>>>
    @GET("/message/user/{user_id}")fun getAllMessages(@Path("user_id")user_id:Long) : Call<ApiResult<List<Message>>>
    @GET("/message/received/user/{user_id}")fun getReceived(@Path("user_id")user_id:Long) : Call<ApiResult<List<Message>>>
    @GET("/message/sent/user/{user_id}") fun getSent(@Path("user_id")user_id:Long) :  Call<ApiResult<List<Message>>>

    @GET("/profile/user/{nickname}")fun getProfile(@Path("nickname") nickname:String) : Call<ApiResult<Profile>>
    @POST("/profile/user/{member_id}")fun createProfile(@Path("member_id")member_id:Long,@Body profileForm: ProfileForm) : Call<ApiResult<String>>
    @PUT("/profile/user/{member_id}")fun changeProfile(@Path("member_id")member_id:Long,@Body profileForm: ProfileForm): Call<ApiResult<String>>

    @PUT("/reply/{reply_id}")fun editReply(@Path("reply_id")reply_id:Long,@Body replyForm:ReplyForm):Call<ApiResult<Reply>>
    @DELETE("/reply/{reply_id}")fun deleteReply(@Path("reply_id")reply_id:Long) : Call<ApiResult<String>>
    @POST("/reply/comment/{comment_id}")fun writeReply(@Path("comment_id")comment_id:Long,@Body replyForm: ReplyForm) : Call<ApiResult<Reply>>

    @POST("/user/signIn")fun signIn(@Body signInForm: SignInForm):Call <ApiResult<Member>>
    @POST("/user/signUp")fun signUp(@Body signUpForm: SignUpForm):Call <ApiResult<String>>
    @GET("/user/getUserId")fun getUserId():Call<Long>

    companion object {
        val instance = RestApiServiceGenerator.createService(RestApiService::class.java)
    }
}