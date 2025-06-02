package com.example.incidencesapp.models

data class IncidenceRequest(
    val title: String,
    val description: String,
    val priority: String,
    val status: String,
    val date: String? = null
)