package com.android.gotripmap.data.network

import com.android.gotripmap.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.reflect.Type

object RouteUpdateAPIFactory {
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

  // Define a custom exception for 404 errors
  class NotFoundException(message: String) : IOException(message)

  // Custom converter to handle 404 errors
  class ErrorHandlingConverter(private val gson: Gson) : Converter.Factory() {
    override fun responseBodyConverter(
      type: Type,
      annotations: Array<Annotation>,
      retrofit: Retrofit
    ): Converter<ResponseBody, *> {
      val delegate = retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
      return Converter { body ->
        if (body.contentLength() == 0L) {
          // Handle empty response body
          throw NotFoundException("Empty response body")
        } else {
          try {
            delegate.convert(body)
          } catch (e: HttpException) {
            // Check if it's a 404 error
            if (e.code() == 404) {
              throw NotFoundException("Resource not found")
            } else {
              // Re-throw other HTTP errors
              throw e
            }
          }
        }
      }
    }
  }

  val apiService = retrofit.newBuilder()
    .addConverterFactory(ErrorHandlingConverter(Gson()))
    .build()
    .create(RouteUpdateApiService::class.java)

}
