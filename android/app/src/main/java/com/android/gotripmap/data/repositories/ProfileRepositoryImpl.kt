package com.android.gotripmap.data.repositories

import android.content.Context
import android.util.Log
import androidx.datastore.dataStore
import com.android.gotripmap.R
import com.android.gotripmap.data.network.AuthApiService
import com.android.gotripmap.data.network.OTPApiService
import com.android.gotripmap.data.network.UserDataAPIService
import com.android.gotripmap.data.pojo.AuthResponse
import com.android.gotripmap.data.pojo.OTPRequest
import com.android.gotripmap.data.pojo.OTPResponse
import com.android.gotripmap.data.serializers.ProfileSerializer
import com.android.gotripmap.domain.entities.Profile
import com.android.gotripmap.domain.entities.Status
import com.android.gotripmap.domain.repositories.StatusRepository
import com.android.gotripmap.domain.repositories.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

class ProfileRepositoryImpl(
  private val context: Context,
  private val userDataAPIService: UserDataAPIService,
  private val coroutineScope: CoroutineScope,
  private val authApiService: AuthApiService,
  private val otpApiService: OTPApiService,
  private val statusRepository: StatusRepository
) : ProfileRepository {

  private val Context.profileDataStore by dataStore(DATASTORE_NAME, ProfileSerializer)


  companion object {
    private const val DATASTORE_NAME = "profile_datastore.json"
  }

  override val profile: Flow<Profile> = context.profileDataStore.data

  override suspend fun startAuth(profile: Profile): AuthResponse? {
    return try {
      authApiService.authentification(profile)
    } catch (e: Exception) {
      statusRepository.handle(e)
      null
    }
  }

  override suspend fun otpAuth(id: Int, code: String): OTPResponse? {
    return if (statusRepository.isConnected.first()) {
       try {
        otpApiService.otp(OTPRequest(id, code))
      } catch (e: Exception) {
        statusRepository.handle(e)
        null
      }
    } else {
      statusRepository.setStatus(Status(R.string.connection_lost))
      null
    }
  }

  override suspend fun updateProfileInfo(
    profile: Profile
  ) {
    context.profileDataStore.updateData {
      profile
    }
    Log.w("profilehash",profile.hash)
    Log.w("connectvity",statusRepository.isConnected.first().toString())

    if (profile.hash.isNotEmpty()) {
      if (statusRepository.isConnected.first()) {
        coroutineScope.launch {
          try {
            userDataAPIService.userData(profile)
          } catch (e: Exception) {
            statusRepository.handle(e)
          }
        }
      } else {
        statusRepository.setStatus(Status(R.string.connection_lost))
      }
    }
  }

}
