package com.android.gotripmap.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.android.gotripmap.domain.usecases.status.GetStatusUseCase
import kotlinx.coroutines.flow.receiveAsFlow

class MainVM(getStatusUseCase: GetStatusUseCase): ViewModel() {
  val status = getStatusUseCase().receiveAsFlow()
}
