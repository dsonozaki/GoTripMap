package com.android.gotripmap.data.network

import com.android.gotripmap.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object EntriesAPIFactory {
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
    .baseUrl(BuildConfig.SERVER_ADDRESS)
    .build()

  val apiService = retrofit.create(EntriesAPIService::class.java)

}
