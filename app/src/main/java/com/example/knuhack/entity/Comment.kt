package com.example.knuhack.entity

import java.time.LocalDateTime

data class Comment (
    var commentId : Long,
    var author : String,
    var content : String,
    var replyDtoList : List<Reply>,
    var time : String
    )
