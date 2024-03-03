package com.android.gotripmap.domain.usecases.voice_recognition

import com.android.gotripmap.domain.repositories.VoiceToTextParser

class GetSpeechUseCase(private val voiceToTextParser: VoiceToTextParser) {
  operator fun invoke() = voiceToTextParser.state
}
