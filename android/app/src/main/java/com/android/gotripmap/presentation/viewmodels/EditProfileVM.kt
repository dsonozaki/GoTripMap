package com.android.gotripmap.presentation.viewmodels

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.gotripmap.domain.entities.Profile
import com.android.gotripmap.domain.usecases.profile.GetProfileUseCase
import com.android.gotripmap.domain.usecases.profile.UpdateProfileUseCase
import com.android.gotripmap.presentation.states.ProfileState
import com.android.gotripmap.presentation.utils.EmailPhoneCorrectChecker
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditProfileVM(
  getProfileUseCase: GetProfileUseCase,
  private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

  private val newProfileFlow = MutableSharedFlow<ProfileState>()

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

  fun changeEmailReceive(receiveEmails: Boolean) {
    viewModelScope.launch {
      newProfileFlow.emit(profile.value.copy(receiveEmails = receiveEmails))
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

  fun initialize() {
    viewModelScope.launch {
      val profileState = profile.value.copy(initialized = true)
      val checker = EmailPhoneCorrectChecker(profileState.phone.text)
      if (checker.isCorrectEmail()) {
        updateProfileUseCase(
          profileState.copy(
            phone = TextFieldValue(""),
            email = profileState.phone
          ).toProfile()
        )
      } else {
        updateProfileUseCase(profileState.toProfile())
      }
    }
  }

}
