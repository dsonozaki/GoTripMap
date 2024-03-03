package com.android.gotripmap.presentation.elements

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.size.Dimension
import com.android.gotripmap.R
import com.android.gotripmap.ui.theme.AppTheme

@Composable
fun UserInputDialogFragment(
  state: MutableState<TextFieldValue>,
  focusRequester: FocusRequester,
  onReady: () -> Unit
) {
  var readOnly by rememberSaveable {
    mutableStateOf(false)
  }
  Row(
    Modifier
      .fillMaxWidth()
      .padding(0.dp, 0.dp, 0.dp, 45.dp),
    horizontalArrangement = Arrangement.End
  ) {
    Column(
      Modifier
        .weight(1f)
        .wrapContentHeight(),
      horizontalAlignment = Alignment.End
    ) {
      val customTextSelectionColors = TextSelectionColors(
        handleColor = AppTheme,
        backgroundColor = AppTheme
      )
      CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        BasicTextField(
          value = state.value,
          onValueChange = { newText: TextFieldValue ->
            state.value = newText
          },
          readOnly = readOnly,
          modifier = Modifier
            .focusRequester(focusRequester)
            .padding(0.dp, 12.dp, 0.dp, 0.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White)
            .padding(13.dp, 13.dp, 10.dp, 12.dp)
            .animateContentSize(),
          textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.End),
        )
      }
      ElevatedButton(
        onClick = {
          onReady()
          readOnly = true
        },
        colors = ButtonDefaults.buttonColors(containerColor = AppTheme, contentColor = Color.Black)
      ) {
        androidx.compose.material.Text(
          stringResource(id = R.string.text_ready),
          style = MaterialTheme.typography.labelLarge
        )
      }
    }

    NoProfileElement(
      64.dp, 30.dp, Modifier
        .padding(11.dp, 0.dp, 0.dp)
        .size(64.dp, 64.dp)
    )
  }
}

