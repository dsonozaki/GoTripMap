package com.android.gotripmap.domain.usecases.routes

import com.android.gotripmap.domain.entities.SearchEntry
import com.android.gotripmap.domain.repositories.EntriesRepository
import kotlinx.coroutines.flow.Flow

class GetHistoryUseCase(private val repository: EntriesRepository) {
  operator fun invoke(): Flow<List<SearchEntry>> = repository.entries
}
