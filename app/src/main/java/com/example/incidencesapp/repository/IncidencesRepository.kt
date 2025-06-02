// repository/IncidencesRepository.kt
package com.example.incidencesapp.repository

import com.example.incidencesapp.models.*
import com.example.incidencesapp.network.RetrofitClient
import com.example.incidencesapp.utils.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class IncidencesRepository(private val sessionManager: SessionManager) {
    private val apiService = RetrofitClient.apiService

    suspend fun login(email: String, password: String): Flow<Result<User>> = flow {
        try {
            val response = apiService.login(LoginRequest(email, password))

            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.data != null && apiResponse.token != null) {
                    sessionManager.saveAuthToken(apiResponse.token)
                    sessionManager.saveUser(apiResponse.data)
                    emit(Result.success(apiResponse.data))
                } else {
                    emit(Result.failure(Exception(apiResponse?.msj ?: "Error desconocido")))
                }
            } else {
                emit(Result.failure(Exception("Error: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun register(username: String, email: String, password: String): Flow<Result<User>> = flow {
        try {
            val response = apiService.register(RegisterRequest(username, email, password))

            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.data != null && apiResponse.token != null) {
                    sessionManager.saveAuthToken(apiResponse.token)
                    sessionManager.saveUser(apiResponse.data)
                    emit(Result.success(apiResponse.data))
                } else {
                    emit(Result.failure(Exception(apiResponse?.msj ?: "Error desconocido")))
                }
            } else {
                emit(Result.failure(Exception("Error: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun getAllIncidences(): Flow<Result<List<Incidence>>> = flow {
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Result.failure(Exception("No hay token de autenticación")))
                return@flow
            }

            val response = apiService.getAllIncidences("Bearer $token")

            if (response.isSuccessful) {
                val apiResponse = response.body()
                emit(Result.success(apiResponse?.data ?: emptyList()))
            } else {
                emit(Result.failure(Exception("Error: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun getMyIncidences(): Flow<Result<List<Incidence>>> = flow {
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Result.failure(Exception("No hay token de autenticación")))
                return@flow
            }

            val response = apiService.getMyIncidences("Bearer $token")

            if (response.isSuccessful) {
                val apiResponse = response.body()
                emit(Result.success(apiResponse?.data ?: emptyList()))
            } else {
                emit(Result.failure(Exception("Error: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun createIncidence(incidence: IncidenceRequest): Flow<Result<Incidence>> = flow {
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Result.failure(Exception("No hay token de autenticación")))
                return@flow
            }

            val response = apiService.createIncidence(incidence, "Bearer $token")

            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.data != null) {
                    emit(Result.success(apiResponse.data))
                } else {
                    emit(Result.failure(Exception("Error al crear incidencia")))
                }
            } else {
                emit(Result.failure(Exception("Error: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun updateIncidence(id: String, incidence: IncidenceRequest): Flow<Result<Incidence>> = flow {
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Result.failure(Exception("No hay token de autenticación")))
                return@flow
            }

            val response = apiService.updateIncidence(id, incidence, "Bearer $token")

            if (response.isSuccessful) {
                val apiResponse = response.body()
                if (apiResponse?.data != null) {
                    emit(Result.success(apiResponse.data))
                } else {
                    emit(Result.failure(Exception("Error al actualizar incidencia")))
                }
            } else {
                emit(Result.failure(Exception("Error: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun deleteIncidence(id: String): Flow<Result<Unit>> = flow {
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Result.failure(Exception("No hay token de autenticación")))
                return@flow
            }

            val response = apiService.deleteIncidence(id, "Bearer $token")

            if (response.isSuccessful) {
                emit(Result.success(Unit))
            } else {
                emit(Result.failure(Exception("Error: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
