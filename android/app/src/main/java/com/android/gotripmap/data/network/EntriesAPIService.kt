package com.android.gotripmap.data.network

import com.android.gotripmap.data.pojo.EntriesUpdate
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface EntriesAPIService {
  @Headers(
    "Connection: close",
    "Content-Type: application/json"
  )
  @POST("/entry")
  suspend fun addEntry(@Body request: EntriesUpdate): Boolean
}
