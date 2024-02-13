package com.example.sosapplication.model

data class ApiErrorResponse(
    val message: String? = null,
    val validationMessages: List<String>? = null,
    val errorCode: Int? = null
)