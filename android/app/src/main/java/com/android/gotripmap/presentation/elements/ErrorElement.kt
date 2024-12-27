package com.android.gotripmap.presentation.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.android.gotripmap.R

/**
 * Индикатор ошибки при загрузке изображения
 */
@Composable
fun ErrorElement(){ //Выглядит некрасиво, позже доделать
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
      Image(
        painter = painterResource(id = R.drawable.baseline_error_outline_24),
        contentDescription = "error label",
      )
      Text(text= stringResource(id = R.string.image_not_loaded),
        style = MaterialTheme.typography.bodySmall )
    }
}
