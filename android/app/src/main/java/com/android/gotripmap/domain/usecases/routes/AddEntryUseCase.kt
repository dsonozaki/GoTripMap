package com.android.gotripmap.domain.usecases.routes

import com.android.gotripmap.data.db.SearchEntryDbModel
import com.android.gotripmap.domain.entities.SearchEntry
import com.android.gotripmap.domain.entities.Transport
import com.android.gotripmap.domain.repositories.EntriesRepository
import java.time.LocalDateTime

class AddEntryUseCase(private val repository: EntriesRepository) {
  suspend operator fun invoke(entry: SearchEntry) =
    repository.addEntry(entry)

}
