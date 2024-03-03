package com.android.gotripmap.domain.entities

data class VoiceToTextParserState(val spokenText: String = "",val isSpeaking : Boolean = false,val error: String? = null)
