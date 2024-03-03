package com.android.gotripmap.data.repositories

import android.health.connect.datatypes.units.Length
import com.android.gotripmap.data.db.MainDAO
import com.android.gotripmap.data.db.SearchEntryDbModel
import com.android.gotripmap.data.mappers.RoutesAndEntriesMapper
import com.android.gotripmap.data.mappers.SearchEntryMapper
import com.android.gotripmap.domain.entities.CurrentEntryRoutes
import com.android.gotripmap.domain.entities.Route
import com.android.gotripmap.domain.entities.SearchEntry
import com.android.gotripmap.domain.entities.Transport
import com.android.gotripmap.domain.repositories.EntriesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.coroutines.coroutineContext

/**
 * Реализация репозитория запросов
 */
class EntriesRepositoryImpl(
  private val mainDAO: MainDAO,
  private val entriesMapper: SearchEntryMapper,
  private val routesAndEntriesMapper: RoutesAndEntriesMapper
) : EntriesRepository {
  /**
   * История запросов
   */
  override val entries: Flow<List<SearchEntry>>
    get() = mainDAO.getAllSearchEntries().map { entriesMapper.mapDbListToDtList(it) }

  /**
   * Данные для отображения на главном экране
   */
  override val currentSearchData: Flow<CurrentEntryRoutes?>
    get() = mainDAO.getCurrentRoutes().map { if (it.isNotEmpty()) routesAndEntriesMapper.mapDbToDtModel(it[0]) else null }

  override suspend fun updateEntry(
    id: Int,
    startPointPlace: String,
    endPointPlace: String,
    length: String
  ) {
    val entry = mainDAO.getEntry(id).first()[0]
    if (entry.length==null) {
      mainDAO.updateEntry(id, startPointPlace, endPointPlace, length)
    } //костыль
  }

  override suspend fun insertEntry(entry: String, dateTime: LocalDateTime, transport: Transport): Long =
    mainDAO.insertEntry(
      SearchEntryDbModel(
        entry = entry,
        dateTime = dateTime.toEpochSecond(ZoneOffset.of(
          ZoneId.systemDefault().rules.getOffset(
            Instant.now()).toString()
        )),
        transport = transport,
        startPointPlace = null,
        endPointPlace = null,
        length = null
      )
    )

  /**
   * Функция для отображения данного запроса на главном экране
   */

  override suspend fun makeEntryCurrent(entryId: Int) {
    mainDAO.makeEntryCurrent(entryId)
  }

  override suspend fun clearHistory() {
    mainDAO.clearHistory()
  }
}
