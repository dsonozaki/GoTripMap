package com.android.gotripmap.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * DAO для базы данных маршрутов и запросов
 */
@Dao
interface MainDAO {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRoute(route: RouteDbModel)

  @Query("DELETE FROM routedbmodel WHERE liked=0 AND searchEntry!=:id")
  suspend fun deleteRecentRoutes(id: Int)

  //Получение списка избранных маршрутов
  @Query("SELECT * FROM routedbmodel WHERE liked=1")
  fun getLikedRoutes(): Flow<List<RouteDbModel>>

  //Получение результата последних запросов
  @Transaction @Query("SELECT * FROM SearchEntryDbModel WHERE ID IN (SELECT currentEntry FROM CurrentEntryDbModel WHERE id=0)")
  fun getCurrentRoutes(): Flow<List<RoutesAndEntryRelation>>

  @Query("SELECT * FROM searchentrydbmodel WHERE id=:id")
  fun getEntry(id: Int): Flow<List<SearchEntryDbModel>>

  @Query("UPDATE routedbmodel SET liked=not liked WHERE id=:id")
  suspend fun changeLiked(id: Int)

  @Query("DELETE FROM searchentrydbmodel WHERE id NOT IN (SELECT currentEntry FROM CurrentEntryDbModel WHERE id=0)")
  suspend fun clearHistory()

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertEntry(searchEntryDbModel: SearchEntryDbModel): Long

  @Query("UPDATE searchentrydbmodel SET startPointPlace=:startPointPlace,endPointPlace=:endPointPlace,length=:length WHERE id=:id")
  suspend fun updateEntry(id: Int,startPointPlace: String,endPointPlace: String,length: String)

  @Query("INSERT OR REPLACE INTO CurrentEntryDbModel (id,currentEntry) VALUES (0,:id)")
  suspend fun makeEntryCurrent(id: Int)

  @Query("SELECT * FROM searchentrydbmodel ORDER BY id DESC")
  fun getAllSearchEntries(): Flow<List<SearchEntryDbModel>>
}
