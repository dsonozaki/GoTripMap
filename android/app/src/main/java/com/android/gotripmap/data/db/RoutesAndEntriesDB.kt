package com.android.gotripmap.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * База данных для Rooms
 */
@Database(entities = [RouteDbModel::class, SearchEntryDbModel::class, CurrentEntryDbModel::class], version = 1)
abstract class RoutesAndEntriesDB : RoomDatabase()  {
  abstract fun mainDao(): MainDAO
  companion object {
    private const val DB_NAME = "my_database"
    fun create(context: Context): RoutesAndEntriesDB {
      return Room.databaseBuilder(
        context,
        RoutesAndEntriesDB::class.java,
        DB_NAME
      ).build()
    }
  }
}
