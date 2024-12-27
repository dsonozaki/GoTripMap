package com.android.gotripmap.data.mappers

import com.android.gotripmap.data.db.SearchEntryDbModel
import com.android.gotripmap.domain.entities.SearchEntry
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Маппер для запроса
 */
class SearchEntryMapper {
  fun mapDbToDtModel(searchEntryDbModel: SearchEntryDbModel) =
    SearchEntry(
      id = searchEntryDbModel.id,
      entry = searchEntryDbModel.entry,
      dateTime = timestampToString(searchEntryDbModel.dateTime),
      transport = searchEntryDbModel.transport,
      startPointPlace = searchEntryDbModel.startPointPlace,
      endPointPlace = searchEntryDbModel.endPointPlace,
      length = searchEntryDbModel.length
    )

  fun mapDtToDbModel(searchEntry: SearchEntry) =
    SearchEntryDbModel(
      id = searchEntry.id,
      entry = searchEntry.entry,
      dateTime = stringToTimestamp(searchEntry.dateTime),
      transport = searchEntry.transport,
      startPointPlace = searchEntry.startPointPlace,
      endPointPlace = searchEntry.endPointPlace,
      length = searchEntry.length
    )

  fun mapDbListToDtList(dbList: List<SearchEntryDbModel>): List<SearchEntry> =
    dbList.map { mapDbToDtModel(it) }

  private fun timestampToString(timestamp: Long) =
    LocalDateTime.ofEpochSecond(timestamp,0, ZoneId.systemDefault().rules.getOffset(
      Instant.now())).format(format)

  private fun stringToTimestamp(date: String) =
    LocalDateTime.parse(date,format).toEpochSecond(ZoneId.systemDefault().rules.getOffset(
      Instant.now()))

  private val format = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm", Locale("ru"))
}
