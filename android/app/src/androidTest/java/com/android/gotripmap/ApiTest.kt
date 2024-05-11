package com.android.gotripmap

import android.util.Log
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.gotripmap.data.db.RouteDbModel
import com.android.gotripmap.data.mappers.RouteMapper
import com.android.gotripmap.data.network.AuthAPIFactory
import com.android.gotripmap.data.network.EntriesAPIFactory
import com.android.gotripmap.data.network.OTPApiFactory
import com.android.gotripmap.data.network.RouteUpdateAPIFactory
import com.android.gotripmap.data.network.SearchAPIFactory
import com.android.gotripmap.data.network.UserDataAPIFactory
import com.android.gotripmap.data.pojo.EntriesUpdate
import com.android.gotripmap.data.pojo.OTPRequest
import com.android.gotripmap.data.pojo.RouteUpdate
import com.android.gotripmap.data.pojo.SearchRequest
import com.android.gotripmap.domain.entities.MyAddress
import com.android.gotripmap.domain.entities.Profile
import com.android.gotripmap.domain.entities.SearchEntry
import com.android.gotripmap.domain.entities.Transport
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class ApiTest {

  private val charPool = ('a'..'z').toList()

  private fun randomStringByKotlinRandom(length: Int) = (1..length)
    .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
    .joinToString("")

  private var testProfile = Profile(phone = "xwenokzkqm",id=1, hash = "V3xcP2k0OqhHmP7tBKuJTY5WYX7fSgGslUagp7cQ5J5or")
  
  @Test
  fun testAuthAPI() = runTest {
    val authApiService = AuthAPIFactory.apiService
    val (profileId, code) = authApiService.authentification(Profile(phone = randomStringByKotlinRandom(10)))
    assert(code.length == 6)
    assert(code.all { it.isDigit() })
    val otpApiService = OTPApiFactory.apiService
    val result = otpApiService.otp(OTPRequest(profileId, code))
    assert(result.profile.hash.isNotEmpty())
    assert(result.profile.id != 0)
    assert(result.profile.initialized)
    try {
      otpApiService.otp(OTPRequest(1111, code))
    } catch (e: HttpException) {
      assert(e.code() == 403)
    }
    try {
      otpApiService.otp(OTPRequest(profileId, "111111"))
    } catch (e: HttpException) {
      assert(e.code() == 403)
    }
  }

  @Test
  fun testRouteUpdateAPI() = runTest {
    val testRoute = RouteDbModel(
      0,
      "12m",
      "route",
      "test",
      "test1",
      "test2",
      "test3",
      "test4",
      "test5",
      Transport.BICYCLE,
      1,
      false
    )

    RouteUpdateAPIFactory.apiService.updateRoute(
      RouteUpdate(
        listOf(testRoute),
        testProfile.id,
        testProfile.hash
      )
    )
    print(testProfile.id)
    try {
      RouteUpdateAPIFactory.apiService.updateRoute(
        RouteUpdate(
          listOf(testRoute),
          testProfile.id,
          "fakehash"
        )
      )
    } catch (e: HttpException) {
      assert(e.code() == 403)
    }
    try {
      RouteUpdateAPIFactory.apiService.updateRoute(
        RouteUpdate(
          listOf(testRoute),
          111,
          testProfile.hash
        )
      )
    } catch (e: HttpException) {
      assert(e.code() == 403)
    }
    val authApiService = AuthAPIFactory.apiService
    val otpApiService = OTPApiFactory.apiService
    val mapper = RouteMapper(Gson())
    val (profileId, code) = authApiService.authentification(testProfile)
    val result = otpApiService.otp(OTPRequest(profileId, code))
    Log.w("routes",result.routes.toString())
    assert(mapper.mapDtListToDbList(result.routes).all { it.copy(id=0) == testRoute })
  }

  @Test
  fun testEntriesUpdateAPI() = runTest {
    val testEntry = SearchEntry(0, "test", "test1", Transport.BICYCLE, "test2", "test3", "test4")
    EntriesAPIFactory.apiService.addEntry(
      EntriesUpdate(
        listOf(testEntry),
        testProfile.id,
        testProfile.hash
      )
    )
    try {
      EntriesAPIFactory.apiService.addEntry(
        EntriesUpdate(
          listOf(testEntry),
          testProfile.id,
          "fakehash"
        )
      )
    } catch (e: HttpException) {
      assert(e.code() == 403)
    }
    try {
      EntriesAPIFactory.apiService.addEntry(EntriesUpdate(listOf(testEntry), 111, testProfile.hash))
    } catch (e: HttpException) {
      assert(e.code() == 403)
    }
    val authApiService = AuthAPIFactory.apiService
    val otpApiService = OTPApiFactory.apiService
    val (profileId, code) = authApiService.authentification(testProfile)
    val result = otpApiService.otp(OTPRequest(profileId, code))
    Log.w("routes_entries",result.entries.toString())

    assert(result.entries.all { it.copy(id=0) == testEntry })
  }

  @Test
  fun testSearchApi() = runTest {
    val searchApi = SearchAPIFactory.apiService
    val result = searchApi.getRoutesForEntry(
      SearchRequest(
        "Хочу зайти в храм, а потом в McDonalds", Transport.PUBLIC,
        MyAddress()
      )
    )
    assert(result.entries.map { it.destpoint.category } == listOf("храм", "mcdonalds"))
  }

  @Test
  fun testUserDataAPI() = runTest {
    val newProfile = testProfile.copy(username = "Абобус")
    UserDataAPIFactory.apiService.userData(newProfile)
    val authApiService = AuthAPIFactory.apiService
    val otpApiService = OTPApiFactory.apiService
    val (profileId, code) = authApiService.authentification(testProfile)
    val result = otpApiService.otp(OTPRequest(profileId, code))
    assert(result.profile.username == "Абобус")
  }
}
