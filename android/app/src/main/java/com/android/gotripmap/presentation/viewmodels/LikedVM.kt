package com.android.gotripmap.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.gotripmap.domain.usecases.routes.ChangeLikedUseCase
import com.android.gotripmap.domain.usecases.routes.GetLikedUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LikedVM(getLikedUseCase: GetLikedUseCase, val changeLikedUseCase: ChangeLikedUseCase): ViewModel() {
  val likedItems = getLikedUseCase().stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    listOf()
  )

  fun changeLiked(id: Int) {
    viewModelScope.launch {
      changeLikedUseCase(id)
    }
  }
}

