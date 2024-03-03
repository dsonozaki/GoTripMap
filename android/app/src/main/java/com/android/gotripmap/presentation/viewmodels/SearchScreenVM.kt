package com.android.gotripmap.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.gotripmap.domain.entities.MyAddress
import com.android.gotripmap.domain.usecases.geoposition.GetAddressesUseCase
import com.android.gotripmap.domain.usecases.routes.ChangeLikedUseCase
import com.android.gotripmap.domain.usecases.routes.DeleteRecentRoutesUseCase
import com.android.gotripmap.domain.usecases.routes.GetCurrentRoutesUseCase
import com.android.gotripmap.domain.usecases.routes.LoadRouteUseCase
import com.android.gotripmap.domain.usecases.routes.UpdateEntryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchScreenVM(
  val changeLikedUseCase: ChangeLikedUseCase,
  getCurrentRoutesUseCase: GetCurrentRoutesUseCase,
  private val getAddresses: GetAddressesUseCase,
  private val loadRouteUseCase: LoadRouteUseCase,
  private val updateEntryUseCase: UpdateEntryUseCase,
  private val deleteRecentRoutesUseCase: DeleteRecentRoutesUseCase
) : ViewModel() {
  val currentEntry = getCurrentRoutesUseCase().map { it?.searchEntry }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    null
  )
  val routes = getCurrentRoutesUseCase().map {
    val routes = it?.routes
    if (!routes.isNullOrEmpty())
      routes.random().let {randomRoute ->
        updateEntryUseCase(
          currentEntry.value!!.id,
          randomRoute.startPointPlace,
          randomRoute.endPointPlace,
          randomRoute.length
        )
      }
    routes
  }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    listOf()
  )

  fun loadData() {
    viewModelScope.launch {
      try {
        loadRouteUseCase(
          currentEntry.value ?: throw Exception("unexpected empty search string"),
          location.value
        )
        deleteRecentRoutesUseCase(currentEntry.value!!.id)
      } catch (e: Exception) {
        Log.w("problem", e.stackTraceToString())
      }
    }
  }

  val location = MutableStateFlow(MyAddress())

  fun getLocation() {
    viewModelScope.launch {
      getAddresses(1000).collect {
        location.emit(it)
      }
    }
  }

  fun changeLiked(id: Int) {
    viewModelScope.launch {
      changeLikedUseCase(id)
    }
  }
}
