package com.android.gotripmap.data.network

import com.android.gotripmap.data.pojo.RouteResponse
import com.android.gotripmap.data.pojo.SearchRequest
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface ApiService {
@Headers (
  "Connection: close",
  "Content-Type: application/json"
)
  @POST("/")
  suspend fun getRoutesForEntry(@Body request: SearchRequest): RouteResponse
}
