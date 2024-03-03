package com.android.gotripmap.domain.usecases.routes

import com.android.gotripmap.domain.repositories.RoutesRepository

class ChangeLikedUseCase(private val repository: RoutesRepository) {
  suspend operator fun invoke(id: Int) = repository.changeLiked(id)
}
