package com.android.gotripmap.domain.usecases.status

import com.android.gotripmap.domain.entities.Status
import com.android.gotripmap.domain.repositories.StatusRepository
import kotlinx.coroutines.channels.Channel

class GetStatusUseCase(private val repository: StatusRepository) {
  operator fun invoke(): Channel<Status?> {
    return repository.statusChannel
  }
}
