package com.android.gotripmap.domain.usecases.voice_recognition

import com.android.gotripmap.domain.repositories.VoiceToTextParser

class StartListeningUseCase(private val voiceToTextParser: VoiceToTextParser) {
  operator fun invoke(languageCode: String) {
    voiceToTextParser.startListening(languageCode)
  }
}
