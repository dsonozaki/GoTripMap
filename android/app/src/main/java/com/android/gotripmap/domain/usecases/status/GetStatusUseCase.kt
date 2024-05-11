package com.android.gotripmap.domain.usecases.connectivity

import com.android.gotripmap.domain.repositories.StatusRepository

class CheckConnectionUseCase(private val repository: StatusRepository) {
  operator fun invoke() {
    repository.isConnected
  }
}
