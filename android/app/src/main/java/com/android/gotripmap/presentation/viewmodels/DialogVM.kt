package com.android.gotripmap.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.gotripmap.domain.entities.Transport
import com.android.gotripmap.domain.usecases.routes.CreateEntryUseCase
import com.android.gotripmap.domain.usecases.routes.MakeEntryCurrentUseCase
import com.android.gotripmap.domain.usecases.voice_recognition.GetSpeechUseCase
import com.android.gotripmap.domain.usecases.voice_recognition.StartListeningUseCase
import com.android.gotripmap.domain.usecases.voice_recognition.StopListeningUseCase
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class DialogVM(
  getSpeechUseCase: GetSpeechUseCase,
  private val startListeningUseCase: StartListeningUseCase,
  private val stopListeningUseCase: StopListeningUseCase,
  private val makeEntryCurrentUseCase: MakeEntryCurrentUseCase,
  private val createEntryUseCase: CreateEntryUseCase,
) : ViewModel() {

  val speech = getSpeechUseCase()
  fun addEntry(entry: String, dateTime: LocalDateTime,transport: Transport) {
    viewModelScope.launch {
      val id = createEntryUseCase(entry, dateTime,transport)
      makeEntryCurrentUseCase(id.toInt())
    }
  }

  fun startListening(languageCode: String) {
    startListeningUseCase(languageCode)
  }

  fun stopListening() {
    stopListeningUseCase()
  }

}
