package com.android.gotripmap.data.network

import com.android.gotripmap.data.pojo.AuthResponse
import com.android.gotripmap.domain.entities.Profile
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApiService {
  @Headers(
    "Connection: close",
    "Content-Type: application/json"
  )
  @POST("/auth")
  suspend fun authentification(@Body request: Profile): AuthResponse
}
