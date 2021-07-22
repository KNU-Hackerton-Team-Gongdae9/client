package com.example.knuhack.entity

data class Board(
    var boardId : Long,
    var category : String,
    var title : String,
    var content : String,
    var author : String,
    var dateTime : String,
)