package com.android.gotripmap.domain.usecases.routes

import com.android.gotripmap.domain.repositories.EntriesRepository

class UpdateEntryUseCase(private val repository: EntriesRepository) {
  suspend operator fun invoke(id: Int, startPointPlace: String,endPointPlace: String, length: String) {
    repository.updateEntry(id,startPointPlace, endPointPlace, length)
  }
}
