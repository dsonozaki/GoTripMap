package com.android.gotripmap.data.db

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Отношение таблиц запросов и маршрутов
 */
data class RoutesAndEntryRelation(
  @Embedded val entry: SearchEntryDbModel,
  @Relation(
    parentColumn = "id",
    entityColumn = "searchEntry"
  )
  val searchEntries: List<RouteDbModel>
)

