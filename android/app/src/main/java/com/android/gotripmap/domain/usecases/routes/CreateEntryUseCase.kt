package com.android.gotripmap.domain.usecases.routes

import com.android.gotripmap.domain.entities.Transport
import com.android.gotripmap.domain.repositories.EntriesRepository
import java.time.LocalDateTime

class CreateEntryUseCase(private val repository: EntriesRepository) {
  suspend operator fun invoke(entry: String, dateTime: LocalDateTime,transport: Transport): Long =
    repository.createEntry(entry,dateTime,transport)

}
