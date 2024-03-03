package com.android.gotripmap.data.repositories

import android.content.Context
import android.util.Log
import androidx.datastore.dataStore
import com.android.gotripmap.data.serializers.ProfileSerializer
import com.android.gotripmap.domain.entities.Profile
import com.android.gotripmap.domain.repositories.ProfileRepository
import kotlinx.coroutines.flow.Flow

class ProfileRepositoryImpl(private val context: Context) : ProfileRepository {

  private val Context.profileDataStore by dataStore(DATASTORE_NAME, ProfileSerializer)


  companion object {
    private const val DATASTORE_NAME = "profile_datastore.json"
  }

  override val profile: Flow<Profile> = context.profileDataStore.data

  override suspend fun createProfile(id: Int, username: String, phone: String) {
    context.profileDataStore.updateData {
      Profile(username, phone)
    }
  }

  override suspend fun updateProfileInfo(
    profile: Profile
  ) {
    Log.w("username_repo",profile.username)
    context.profileDataStore.updateData {
      profile
    }
  }

}
