package com.android.gotripmap.data.repositories

import com.android.gotripmap.R
import com.android.gotripmap.data.db.MainDAO
import com.android.gotripmap.data.db.SearchEntryDbModel
import com.android.gotripmap.data.mappers.RoutesAndEntriesMapper
import com.android.gotripmap.data.mappers.SearchEntryMapper
import com.android.gotripmap.data.network.EntriesAPIService
import com.android.gotripmap.data.pojo.EntriesUpdate
import com.android.gotripmap.domain.entities.CurrentEntryRoutes
import com.android.gotripmap.domain.entities.SearchEntry
import com.android.gotripmap.domain.entities.Status
import com.android.gotripmap.domain.entities.Transport
import com.android.gotripmap.domain.repositories.StatusRepository
import com.android.gotripmap.domain.repositories.EntriesRepository
import com.android.gotripmap.domain.repositories.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * Реализация репозитория запросов
 */
class EntriesRepositoryImpl(
  private val mainDAO: MainDAO,
  private val entriesMapper: SearchEntryMapper,
  private val routesAndEntriesMapper: RoutesAndEntriesMapper,
  private val entriesAPIService: EntriesAPIService,
  private val statusRepository: StatusRepository,
  private val coroutineScope: CoroutineScope,
  private val profileRepository: ProfileRepository
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
    get() = mainDAO.getCurrentRoutes()
      .map { if (it.isNotEmpty()) routesAndEntriesMapper.mapDbToDtModel(it[0]) else null }

  override suspend fun updateEntry(
    id: Int,
    startPointPlace: String,
    endPointPlace: String,
    length: String
  ) {
    val entry = mainDAO.getEntry(id).first()[0]
    if (entry.length == null) {
      mainDAO.updateEntry(id, startPointPlace, endPointPlace, length)
    }
    coroutineScope.launch {
      val profile = profileRepository.profile.first()
      if (profile.hash.isNotEmpty()) {
        if (statusRepository.isConnected.first()) {
          try {
            entriesAPIService.addEntry(
              EntriesUpdate(
                entriesMapper.mapDbListToDtList(
                  mainDAO.getAllSearchEntries().first()
                ).filter { it.endPointPlace!=null }, profile.id, profile.hash
              )
            )
          } catch (e: Exception) {
            statusRepository.handle(e)
          }
        } else {
          statusRepository.setStatus(Status(R.string.connection_lost))
        }
      }
    }
  }

  override suspend fun createEntry(
    entry: String,
    dateTime: LocalDateTime,
    transport: Transport
  ): Long =
    mainDAO.insertEntry(
      SearchEntryDbModel(
        entry = entry,
        dateTime = dateTime.toEpochSecond(
          ZoneOffset.of(
            ZoneId.systemDefault().rules.getOffset(
              Instant.now()
            ).toString()
          )
        ),
        transport = transport,
        startPointPlace = null,
        endPointPlace = null,
        length = null
      )
    )

  override suspend fun addEntry(entry: SearchEntry) {
    mainDAO.insertEntry(entriesMapper.mapDtToDbModel(entry))
  }

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
