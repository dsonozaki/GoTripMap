package com.android.gotripmap.domain.repositories

import com.android.gotripmap.domain.entities.CurrentEntryRoutes
import com.android.gotripmap.domain.entities.SearchEntry
import com.android.gotripmap.domain.entities.Transport
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface EntriesRepository {
  val entries: Flow<List<SearchEntry>>
  suspend fun clearHistory()

  suspend fun makeEntryCurrent(entryId: Int)

  val currentSearchData: Flow<CurrentEntryRoutes?>

  suspend fun insertEntry(entry: String, dateTime: LocalDateTime, transport: Transport): Long
  suspend fun updateEntry(id: Int, startPointPlace: String, endPointPlace: String, length: String)
}
