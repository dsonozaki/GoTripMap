package com.android.gotripmap.presentation.elements

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.gotripmap.R
import com.android.gotripmap.ui.theme.AppTheme

@Composable
fun ReceiveEmails(checked: Boolean, enabled: Boolean, changeEmailReceiving: (Boolean)-> Unit) {
  Row(
    Modifier
      .padding(0.dp,12.dp,0.dp,20.dp)
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Checkbox(
      checked = checked,
      onCheckedChange = {
        Log.w("changeEmail",it.toString())
        Log.w("changeEmail","Enabled $enabled")
        changeEmailReceiving(it)
      },
      enabled=enabled,
      colors = CheckboxDefaults.colors(checkedColor = AppTheme),
      modifier = Modifier.size(24.dp).padding(8.dp)
    )
    Text(
      stringResource(R.string.getEmails), style = MaterialTheme.typography.bodySmall, modifier =
      Modifier.padding(14.dp, 0.dp)
    )
  }
}
