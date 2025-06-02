package com.example.incidencesapp.models

data class ApiResponse<T>(
    val message: String? = null,
    val msj: String? = null,
    val data: T? = null,
    val token: String? = null,
    val count: Int? = null,
    val error: Any? = null
)