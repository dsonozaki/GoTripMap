package com.android.gotripmap.domain.usecases.routes

import com.android.gotripmap.domain.entities.MyAddress
import com.android.gotripmap.domain.entities.SearchEntry
import com.android.gotripmap.domain.repositories.RoutesRepository

class LoadRouteUseCase(private val routesRepository: RoutesRepository) {
  suspend operator fun invoke(entry: SearchEntry,coordinates: MyAddress) = routesRepository.loadRoutesForEntry(entry,coordinates)
}
