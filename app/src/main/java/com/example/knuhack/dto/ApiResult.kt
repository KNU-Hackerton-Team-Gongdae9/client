package com.example.knuhack.dto

data class ApiResult<T>(
    var success : Boolean,
    var response : T,
    var error : ApiError
)
