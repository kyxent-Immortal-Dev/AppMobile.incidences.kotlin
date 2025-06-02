// network/ApiService.kt
package com.example.incidencesapp.network

import com.example.incidencesapp.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<User>>

    @POST("api/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<User>>

    @GET("api/incidences")
    suspend fun getAllIncidences(@Header("Authorization") token: String): Response<ApiResponse<List<Incidence>>>

    @GET("api/incidences/my")
    suspend fun getMyIncidences(@Header("Authorization") token: String): Response<ApiResponse<List<Incidence>>>

    @GET("api/incidences/{id}")
    suspend fun getIncidenceById(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Incidence>>

    @POST("api/incidences")
    suspend fun createIncidence(
        @Body request: IncidenceRequest,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Incidence>>

    @PUT("api/incidences/{id}")
    suspend fun updateIncidence(
        @Path("id") id: String,
        @Body request: IncidenceRequest,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Incidence>>

    @DELETE("api/incidences/{id}")
    suspend fun deleteIncidence(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Unit>>
}

