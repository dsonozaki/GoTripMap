package com.android.gotripmap.data.network

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.connection.ConnectInterceptor.intercept
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object APIFactory {
  private const val SERVER_IP = "http://158.160.116.195"
  private val retrofit = Retrofit.Builder().callbackExecutor {  }
    .client(
    OkHttpClient.Builder()
      .retryOnConnectionFailure(true)
      .addNetworkInterceptor(Interceptor {
        val request = it.request().newBuilder().build()
        it.proceed(request)
      })
      .build()
  )
    .addConverterFactory(
      GsonConverterFactory.create(
        GsonBuilder()
          .setLenient()
          .create()
      )
    )
    .baseUrl(SERVER_IP)
    .build()

  val apiService = retrofit.create(ApiService::class.java)

}
