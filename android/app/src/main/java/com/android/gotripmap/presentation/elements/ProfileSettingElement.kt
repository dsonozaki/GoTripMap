package com.android.gotripmap.presentation.elements

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.android.gotripmap.ui.theme.AppTheme

@Composable
fun ProfileSettingElement(
  editable: Boolean,
  description: String,
  text: TextFieldValue,
  modifier: Modifier = Modifier,
  changeProfileElement: (TextFieldValue) -> Unit
) {
  Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
    Text(description, style = MaterialTheme.typography.bodyMedium)
    val customTextSelectionColors = TextSelectionColors(
      handleColor = AppTheme,
      backgroundColor = AppTheme
    )
    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
      BasicTextField(
        value = text,
        onValueChange = {
          changeProfileElement(it)
        },
        readOnly = !editable,
        textStyle = MaterialTheme.typography.bodyMedium,
        maxLines = 1,
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color.White)
          .padding(14.dp, 10.dp)
      )
    }
  }
}
