package com.example.knuhack.dto

import com.example.knuhack.entity.Message
import com.example.knuhack.entity.WriteComment

data class Member (

    var id : Long,

    var email : String,
    var password : String,
    var username : String,
    var nickname : String,
    var studentId : String,

    var grade : Int,

    var Major : String,
    var MemberRole : String,
    var commentList : List<WriteComment>,


    var sentMessageList : ArrayList<Message>,

    var receivedMessageList : ArrayList<Message>



)



