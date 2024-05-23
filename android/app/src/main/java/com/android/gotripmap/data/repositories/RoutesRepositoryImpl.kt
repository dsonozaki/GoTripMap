package com.android.gotripmap.data.repositories

import com.android.gotripmap.R
import com.android.gotripmap.data.db.MainDAO
import com.android.gotripmap.data.mapkit.SearchMap
import com.android.gotripmap.data.mappers.RouteMapper
import com.android.gotripmap.data.network.RouteUpdateApiService
import com.android.gotripmap.data.network.SearchApiService
import com.android.gotripmap.data.pojo.RouteUpdate
import com.android.gotripmap.data.pojo.SearchRequest
import com.android.gotripmap.domain.entities.MyAddress
import com.android.gotripmap.domain.entities.Route
import com.android.gotripmap.domain.entities.RouteIntermediateResults
import com.android.gotripmap.domain.entities.SearchEntry
import com.android.gotripmap.domain.entities.Status
import com.android.gotripmap.domain.repositories.StatusRepository
import com.android.gotripmap.domain.repositories.ProfileRepository
import com.android.gotripmap.domain.repositories.RoutesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RoutesRepositoryImpl(
  private val mainDAO: MainDAO,
  private val routeMapper: RouteMapper,
  private val searchApiService: SearchApiService,
  private val routeUpdateApiService: RouteUpdateApiService,
  private val coroutineScope: CoroutineScope,
  private val statusRepository: StatusRepository,
  private val profileRepository: ProfileRepository
) : RoutesRepository {


  override val likedRoutes: Flow<List<Route>>
    get() = mainDAO.getLikedRoutes().map { routeMapper.mapDbListToDtList(it) }

  override suspend fun loadRoutesForEntry(entry: SearchEntry, coordinates: MyAddress) {
    val response = if (statusRepository.isConnected.first()) {
      try {
        searchApiService.getRoutesForEntry(SearchRequest(entry.entry, entry.transport, coordinates))
      } catch (e: Exception) {
        statusRepository.handle(e)
        return
      }
    } else {
      statusRepository.setStatus(Status(R.string.connection_lost))
      return
    }
    SearchMap(
      response,
      coordinates,
      entry
    ) { routeIntermadiateResults: RouteIntermediateResults ->
      val routeResult =
        routeMapper.mapApiToDbModel(routeIntermadiateResults, entry)
      coroutineScope.launch {
        mainDAO.insertRoute(routeResult)
      }
    }
  }

  override suspend fun addRoutes(routes: List<Route>) {
    val toInsert = routeMapper.mapDtListToDbList(routes)
    toInsert.forEach {
      mainDAO.insertRoute(it)
    }
  }

  override suspend fun changeLiked(id: Int) {
    mainDAO.changeLiked(id)
    coroutineScope.launch {
      val profile = profileRepository.profile.first()
      if (profile.token.isNotEmpty()) {
        if (statusRepository.isConnected.first()) {
          try {
            routeUpdateApiService.updateRoute(
              RouteUpdate(
                mainDAO.getLikedRoutes().first(),
                profile.id,
                profile.token
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

  override suspend fun deleteRecentRoutes(id: Int) {
    mainDAO.deleteRecentRoutes(id)
    coroutineScope.launch {
      val profile = profileRepository.profile.first()
      if (profile.token.isNotEmpty()) {
        if (statusRepository.isConnected.first()) {
          try {
            routeUpdateApiService.updateRoute(
              RouteUpdate(
                mainDAO.getLikedRoutes().first(),
                profile.id,
                profile.token
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
}
