package com.android.gotripmap.data.network

import com.android.gotripmap.data.pojo.RouteResponse
import com.android.gotripmap.data.pojo.RouteUpdate
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RouteUpdateApiService {
  @Headers(
    "Connection: close",
    "Content-Type: application/json"
  )
  @POST("/updateRoute")
  suspend fun updateRoute(@Body request: RouteUpdate): Boolean
}
