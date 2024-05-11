package com.android.gotripmap.domain.usecases.profile

import com.android.gotripmap.domain.entities.Profile
import com.android.gotripmap.domain.repositories.ProfileRepository

class StartAuthUseCase (private val repository: ProfileRepository) {
  suspend operator fun invoke(profile: Profile) = repository.startAuth(profile)
}
