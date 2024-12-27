package com.android.gotripmap.presentation.viewmodels

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.gotripmap.domain.usecases.profile.GetProfileUseCase
import com.android.gotripmap.domain.usecases.profile.StartAuthUseCase
import com.android.gotripmap.domain.usecases.profile.StartOTPUseCase
import com.android.gotripmap.domain.usecases.profile.UpdateProfileUseCase
import com.android.gotripmap.domain.usecases.routes.AddEntryUseCase
import com.android.gotripmap.domain.usecases.routes.AddRoutesUseCase
import com.android.gotripmap.presentation.states.ProfileState
import com.android.gotripmap.presentation.utils.EmailPhoneCorrectChecker
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditProfileVM(
  getProfileUseCase: GetProfileUseCase,
  private val updateProfileUseCase: UpdateProfileUseCase,
  private val startAuthUseCase: StartAuthUseCase,
  private val startOtpUseCase: StartOTPUseCase,
  private val addEntryUseCase: AddEntryUseCase,
  private val addRoutesUseCase: AddRoutesUseCase
) : ViewModel() {

  private val newProfileFlow = MutableSharedFlow<ProfileState>()
  private val _codeFlow = MutableStateFlow("")
  val codeFlow = _codeFlow.asStateFlow()

  val profile = merge(getProfileUseCase().map { ProfileState(it) }, newProfileFlow).stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    ProfileState()
  )

  fun update(profileState: ProfileState) {
    viewModelScope.launch {
      updateProfileUseCase(profileState.toProfile())
    }
  }

  fun updatePhoto(photo: String) {
    viewModelScope.launch {
      newProfileFlow.emit(profile.value.copy(photo = photo))
    }
  }

  fun updateUserName(userName: TextFieldValue) {
    viewModelScope.launch {
      newProfileFlow.emit(profile.value.copy(username = userName))
    }
  }

  fun updatePhone(phone: TextFieldValue) {
    viewModelScope.launch {
      newProfileFlow.emit(profile.value.copy(phone = phone))
    }
  }

  fun updateEmail(email: TextFieldValue) {
    viewModelScope.launch {
      newProfileFlow.emit(profile.value.copy(email = email))
    }
  }

  fun startOTP(code: String) {
    viewModelScope.launch {
      val result = startOtpUseCase(profile.value.id, code) ?: return@launch
      viewModelScope.launch {
        updateProfileUseCase(result.profile)
      }
      viewModelScope.launch {
        addRoutesUseCase(result.routes)
      }
      viewModelScope.launch {
        result.entries.forEach {
          addEntryUseCase(it)
        }
      }
    }
  }

  fun startAuth() {
    viewModelScope.launch {
      val profileState = profile.value.copy(initialized = true)
      val checker = EmailPhoneCorrectChecker(profileState.phone.text)
      val profileResult = if (checker.isCorrectEmail()) {
          profileState.copy(
            phone = TextFieldValue(""),
            email = profileState.phone
          ).toProfile()

      } else {
     profileState.toProfile()
      }
      val (profileId,code) = startAuthUseCase(profileResult)?: return@launch
      profileState.id = profileId
      newProfileFlow.emit(profileState)
      _codeFlow.emit(code)
    }
  }
}
