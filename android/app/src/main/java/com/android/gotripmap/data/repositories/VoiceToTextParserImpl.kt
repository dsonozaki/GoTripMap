package com.android.gotripmap.data.repositories

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.android.gotripmap.domain.entities.VoiceToTextParserState
import com.android.gotripmap.domain.repositories.VoiceToTextParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VoiceToTextParserImpl(
  private val appContext: Context,
  private val ioScope: CoroutineScope
) : RecognitionListener, VoiceToTextParser {

  private val _state = MutableStateFlow(VoiceToTextParserState())
  override val state = _state.asStateFlow()
  val reconizer = SpeechRecognizer.createSpeechRecognizer(appContext)

  override fun startListening(languageCode: String) {
    _state.update { VoiceToTextParserState() }

    if (!SpeechRecognizer.isRecognitionAvailable(appContext)) {
      _state.update {
        it.copy(error = "Recognition unavailable")
      }
    }
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
      putExtra(
        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
      )
      putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
      putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
    }
    reconizer.setRecognitionListener(this)
    reconizer.startListening(intent)
    _state.update {
      it.copy(isSpeaking = true)
    }
  }

  override fun stopListening() {
    reconizer.stopListening()
    _state.update {
      it.copy(isSpeaking = false)
    }
  }

  override fun onReadyForSpeech(params: Bundle?) {
    _state.update {
      it.copy(error = null)
    }
  }

  override fun onBeginningOfSpeech() {}

  override fun onRmsChanged(rmsdB: Float) {}

  override fun onBufferReceived(buffer: ByteArray?) {}

  override fun onEndOfSpeech() {}

  override fun onError(error: Int) {
    _state.update {
      it.copy(error = "Error: $error")
    }
  }

  override fun onResults(results: Bundle?) {
    results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
      ?.forEach { result ->
        _state.update {
          it.copy(spokenText = result)
        }
      }
    ioScope.launch {
      delay(100)
      _state.update {
        it.copy(isSpeaking = false)
      }
    }
  }

  override fun onPartialResults(partialResults: Bundle?) {
    val data = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
    val word = data!![data.size - 1] as String
    _state.update {
      it.copy(spokenText = word)
    }
  }

  override fun onEvent(eventType: Int, params: Bundle?) {}

}
