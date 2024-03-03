package com.android.gotripmap.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.gotripmap.domain.usecases.routes.GetHistoryUseCase
import com.android.gotripmap.domain.usecases.routes.MakeEntryCurrentUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryVM(getHistoryUseCase: GetHistoryUseCase, private val makeEntryCurrentUseCase: MakeEntryCurrentUseCase): ViewModel() {
  val requestsHistory = getHistoryUseCase().stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    listOf()
  )

  fun makeEntryCurrent(id: Int) {
    viewModelScope.launch {
      makeEntryCurrentUseCase(id)
    }
  }
}
