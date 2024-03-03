package com.android.gotripmap.presentation.elements

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.android.gotripmap.R

@Preview
@Composable
fun BobaDialogElement(@PreviewParameter(BobaPreviewProvider::class) text: String) {

  Row(
    Modifier
      .fillMaxWidth().padding(0.dp, 0.dp,0.dp,45.dp),
    horizontalArrangement = Arrangement.Start) {
    Image(
      painterResource(id = R.drawable.boba),
      contentDescription = "Boba",
      modifier = Modifier.padding(0.dp,0.dp,11.dp,0.dp).size(64.dp,64.dp),
      alignment = Alignment.TopStart
    )
    Box(modifier = Modifier
      .clip(RoundedCornerShape(30.dp)).background(Color.White),
    ) {
      Text(text=text,modifier = Modifier.padding(13.dp,13.dp,10.dp,12.dp),style= MaterialTheme.typography.bodyLarge)
    }
  }
}

class BobaPreviewProvider(): PreviewParameterProvider<String> {
  override val values: Sequence<String> = sequence { listOf("Привет! Меня зовут Биба и я помощник в поиске маршрута","Выбери удобный способ ввода информации и расскажи какой маршрут ты хочешь составить","Каким видом транспорта вы хотели бы воспользоваться?") }

}
