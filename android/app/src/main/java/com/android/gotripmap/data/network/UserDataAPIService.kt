package com.android.gotripmap.data.network

import com.android.gotripmap.data.pojo.UserDataUpdate
import com.android.gotripmap.domain.entities.Profile
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserDataAPIService {
  @Headers(
    "Connection: close",
    "Content-Type: application/json"
  )
  @POST("/userData")
  suspend fun userData(@Body request: Profile): Boolean
}
