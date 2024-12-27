package com.android.gotripmap.presentation.elements

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.android.gotripmap.R
import com.android.gotripmap.domain.entities.VoiceToTextParserState
import com.android.gotripmap.presentation.bottom_navigation.BottomItem
import com.android.gotripmap.ui.theme.AppTheme

@Composable
fun FloatingButtons(
  modifier: Modifier,
  navHostController: NavHostController,
  focusRequester: FocusRequester,
  onStart: () -> Unit,
  onStop: () -> Unit,
  speech: State<VoiceToTextParserState>,
  text: MutableState<TextFieldValue>
) {
  var canRecord by rememberSaveable {
    mutableStateOf(false)
  }
  val voicePermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission(),
    onResult = { granted ->
      canRecord = granted
      if (canRecord) {
        onStart()
      }
    })
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    FloatingActionButton(
      onClick = { navHostController.navigate(BottomItem.SEARCH_SCREEN) },
      backgroundColor = Color.Unspecified,
      modifier = Modifier.size(50.dp),
      elevation = FloatingActionButtonDefaults.elevation(
        defaultElevation = 0.dp
      )
    ) {
      Image(
        painterResource(id = R.drawable.exit),
        contentDescription = "exit",
        modifier = Modifier
          .size(50.dp, 50.dp)
      )
    }
    FloatingActionButton(
      onClick = {
        if (!canRecord) {
          voicePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        } else {
          if (speech.value.isSpeaking) {
            onStop()
          } else {
            onStart()
          }
        }
      },
      backgroundColor = AppTheme
    ) {
      Log.w("result_value",speech.value.toString())
      val resource = if (speech.value.isSpeaking) {
        text.value = TextFieldValue(speech.value.spokenText)
        R.drawable.baseline_stop_24
      } else {
        R.drawable.microphone
      }
      Image(
        painterResource(id = resource),
        contentDescription = "microphone",
        modifier = Modifier
          .size(30.dp, 30.dp)
      )
    }
    FloatingActionButton(
      onClick = { focusRequester.requestFocus() },
      backgroundColor = AppTheme
    ) {
      Image(
        painterResource(id = R.drawable.keyboard),
        contentDescription = "keyboard",
        modifier = Modifier
          .size(30.dp, 30.dp)
      )
    }
  }

}
