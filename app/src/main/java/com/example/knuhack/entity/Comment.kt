package com.example.knuhack.entity

data class Comment (
    var id : Long,
    var content : String,
    var author : String,
    var board : Board,
    var commenterList : List<WriteComment>,
    var replyList : List<Reply>
        )
