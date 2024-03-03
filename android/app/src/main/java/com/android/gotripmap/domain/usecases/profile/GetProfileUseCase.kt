package com.android.gotripmap.domain.usecases.profile

import com.android.gotripmap.domain.repositories.ProfileRepository

class GetProfileUseCase (private val repository: ProfileRepository) {
  operator fun invoke() = repository.profile
}
