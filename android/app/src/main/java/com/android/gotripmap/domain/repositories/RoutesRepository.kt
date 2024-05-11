package com.android.gotripmap.domain.repositories

import com.android.gotripmap.domain.entities.MyAddress
import com.android.gotripmap.domain.entities.Route
import com.android.gotripmap.domain.entities.SearchEntry
import kotlinx.coroutines.flow.Flow

interface RoutesRepository {
  val likedRoutes: Flow<List<Route>>
  suspend fun changeLiked(id: Int)
  suspend fun loadRoutesForEntry(entry: SearchEntry, coordinates: MyAddress)
  suspend fun deleteRecentRoutes(id: Int)
  suspend fun addRoutes(routes: List<Route>)
}
