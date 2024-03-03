package com.android.gotripmap.domain.usecases.profile

import android.util.Log
import com.android.gotripmap.domain.entities.Profile
import com.android.gotripmap.domain.repositories.ProfileRepository

class UpdateProfileUseCase (private val repository: ProfileRepository) {
  suspend operator fun invoke(profile: Profile) {
    repository.updateProfileInfo(profile)
    Log.w("username",profile.username)
  }
}
