package com.android.gotripmap.data.network

import com.android.gotripmap.data.pojo.OTPRequest
import com.android.gotripmap.data.pojo.OTPResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OTPApiService {
  @Headers(
    "Connection: close",
    "Content-Type: application/json"
  )
  @POST("/otp")
  suspend fun otp(@Body request: OTPRequest): OTPResponse
}
