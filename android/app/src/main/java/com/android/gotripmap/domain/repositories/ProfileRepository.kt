package com.android.gotripmap.domain.repositories

import com.android.gotripmap.data.pojo.AuthResponse
import com.android.gotripmap.data.pojo.OTPResponse
import com.android.gotripmap.domain.entities.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

  val profile: Flow<Profile>
  suspend fun updateProfileInfo(profile: Profile)
  suspend fun startAuth(profile: Profile): AuthResponse?
  suspend fun otpAuth(id: Int, code: String): OTPResponse?
}
