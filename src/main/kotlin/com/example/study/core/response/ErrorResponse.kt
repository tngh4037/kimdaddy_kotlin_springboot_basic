package com.example.study.core.response

/**
 * 잘못된 요청 등 오류 상황에서 사용자에게 응답할 일원화된 객체
 */
data class ErrorResponse(
    val message: String,
    val errorType: String = "Invalid Argument"
)