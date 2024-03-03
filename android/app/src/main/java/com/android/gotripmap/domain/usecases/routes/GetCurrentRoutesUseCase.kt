package com.android.gotripmap.domain.usecases.routes

import com.android.gotripmap.domain.entities.CurrentEntryRoutes
import com.android.gotripmap.domain.repositories.EntriesRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentRoutesUseCase(private val entriesRepository: EntriesRepository) {
  operator fun invoke(): Flow<CurrentEntryRoutes?> = entriesRepository.currentSearchData
}
