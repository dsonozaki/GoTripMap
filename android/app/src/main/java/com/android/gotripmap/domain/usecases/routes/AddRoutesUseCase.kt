package com.android.gotripmap.domain.usecases.routes

import com.android.gotripmap.domain.entities.Route
import com.android.gotripmap.domain.repositories.RoutesRepository

class AddRoutesUseCase(private val repository: RoutesRepository) {
  suspend operator fun invoke(routes: List<Route>) = repository.addRoutes(routes)
}
