package com.android.gotripmap.domain.usecases.routes

import com.android.gotripmap.domain.entities.Route
import com.android.gotripmap.domain.repositories.RoutesRepository
import kotlinx.coroutines.flow.Flow

class GetLikedUseCase(private val repository: RoutesRepository) {
  operator fun invoke(): Flow<List<Route>> = repository.likedRoutes
}
