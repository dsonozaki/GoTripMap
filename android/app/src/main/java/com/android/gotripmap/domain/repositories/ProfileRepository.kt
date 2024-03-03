package com.android.gotripmap.domain.repositories

import com.android.gotripmap.domain.entities.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

  val profile: Flow<Profile>
  suspend fun createProfile(id: Int, username: String, phone: String)
  suspend fun updateProfileInfo(profile: Profile)
}
