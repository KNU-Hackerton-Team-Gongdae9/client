package com.example.knuhack.entity

data class Comment (
    var commentId : Long,
    var content : String,
    var author : String,
    var replyList : List<Reply>
        )
