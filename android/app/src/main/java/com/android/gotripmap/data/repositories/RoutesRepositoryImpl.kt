package com.android.gotripmap.data.repositories

import android.util.Log
import com.android.gotripmap.data.db.MainDAO
import com.android.gotripmap.data.mapkit.SearchMap
import com.android.gotripmap.data.mappers.RouteMapper
import com.android.gotripmap.data.network.ApiService
import com.android.gotripmap.data.pojo.RouteResponse
import com.android.gotripmap.data.pojo.SearchRequest
import com.android.gotripmap.domain.entities.MyAddress
import com.android.gotripmap.domain.entities.Route
import com.android.gotripmap.domain.entities.RouteIntermediateResults
import com.android.gotripmap.domain.entities.SearchEntry
import com.android.gotripmap.domain.repositories.RoutesRepository
import com.yandex.mapkit.directions.driving.DrivingRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class RoutesRepositoryImpl(
  private val mainDAO: MainDAO,
  private val routeMapper: RouteMapper,
  private val apiService: ApiService,
  private val coroutineScope: CoroutineScope
) : RoutesRepository {


  override val likedRoutes: Flow<List<Route>>
    get() = mainDAO.getLikedRoutes().map { routeMapper.mapDbListToDtList(it) }

  override suspend fun loadRoutesForEntry(entry: SearchEntry, coordinates: MyAddress) {
    val response =
      apiService.getRoutesForEntry(SearchRequest(entry.entry, entry.transport, coordinates))
    SearchMap(
      response,
      coordinates,
      entry
    ) { length: String, time: String, routeIntermadiateResults: RouteIntermediateResults ->
      val routeResult =
        routeMapper.mapApiToDbModel(length, time, routeIntermadiateResults, entry, response)
      Log.w("result", routeResult.route)
      coroutineScope.launch {
        mainDAO.insertRoute(routeResult)
      }
    }
  }

  override suspend fun changeLiked(id: Int) {
    mainDAO.changeLiked(id)
  }

  override suspend fun deleteRecentRoutes(id: Int) {
    mainDAO.deleteRecentRoutes(id)
  }
}
