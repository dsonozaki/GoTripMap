package com.android.gotripmap.domain.repositories

import com.android.gotripmap.domain.entities.VoiceToTextParserState
import kotlinx.coroutines.flow.StateFlow

interface VoiceToTextParser {
  val state: StateFlow<VoiceToTextParserState>
  fun startListening(languageCode: String)

  fun stopListening()
}
