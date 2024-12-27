package com.android.gotripmap.domain.usecases.profile

import com.android.gotripmap.domain.repositories.ProfileRepository

class StartOTPUseCase (private val repository: ProfileRepository) {
  suspend operator fun invoke(id: Int, code: String) = repository.otpAuth(id,code)
}
