package com.android.gotripmap.presentation.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.gotripmap.R

@Composable
fun ReceiveCodeElement(code: MutableState<TextFieldValue>,buttonActive: MutableState<Boolean>) {
  Column(
    Modifier
      .padding(0.dp, 30.dp, 0.dp, 0.dp)
      .fillMaxWidth(), horizontalAlignment = Alignment.Start
  ) {
    Text(stringResource(R.string.code), style = MaterialTheme.typography.bodyMedium)
    EnterCodeDigitElement(code.value, 6) {
      if (it.text.length <= 6 && it.text.all { digit -> digit.isDigit() }) {
        code.value = it
        buttonActive.value = it.text.length == 6
      }
    }
  }
}

@Composable
fun EnterCodeDigitElement(
  value: TextFieldValue,
  length: Int,
  modifier: Modifier = Modifier,
  keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
  onValueChange: (TextFieldValue) -> Unit,
) {
  val spaceBetweenBoxes = 8.dp

  BasicTextField(modifier = modifier,
    value = value,
    singleLine = true,
    onValueChange = {
      onValueChange(it)
    },
    keyboardOptions = keyboardOptions,
    decorationBox = {
      Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spaceBetweenBoxes),
      ) {
        repeat(length) { index ->
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color.White)
              .weight(1f)
              .height(50.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = value.text.getOrNull(index)?.toString() ?: "",
              textAlign = TextAlign.Center,
              style = MaterialTheme.typography.titleMedium
            )
          }
        }
      }
    })
}
