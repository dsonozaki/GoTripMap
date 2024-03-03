package com.android.gotripmap.presentation.elements

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.android.gotripmap.R
import com.android.gotripmap.presentation.utils.BlurTransformation

@Composable
fun ProfilePhoto(context: Context, photo: String, editing: Boolean, selectImage: () -> Unit) {
  val painter =
    rememberAsyncImagePainter(
      ImageRequest.Builder(LocalContext.current)
        .data(data = "https://images.unsplash.com/photo-1628373383885-4be0bc0172fa?        ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1301&q=80")
        .apply(block = fun ImageRequest.Builder.() {
          transformations(
          )
        }).build()
    )
  Box(
    Modifier
      .padding(0.dp, 0.dp, 0.dp, 12.dp)
      .size(100.dp)
  ) {
    val maxSizeModifier =  Modifier.fillMaxSize().clip(CircleShape)
    SubcomposeAsyncImage(
      model = ImageRequest.Builder(context)
        .data(photo).transformations(
          if (editing) {
            listOf(
              BlurTransformation(
                scale = 0.02f,
                radius = 1
              )
            )
          } else {
            listOf()
          }
        )
        .build(),
      contentDescription = "profile photo",
      modifier =
        if (editing) {
          maxSizeModifier.clickable { selectImage() }
        } else {
          maxSizeModifier
               },
      alignment = Alignment.Center,
      error = {
        Canvas(modifier = Modifier.size(100.dp)) {
          drawCircle(
            color = Color.LightGray
          )
        }
      }
    )
    AnimatedVisibility(visible = editing, Modifier.align(Alignment.Center)) {
      Image(
        modifier = Modifier
          .size(30.dp),
        painter = painterResource(R.drawable.solar_gallery_bold),
        contentDescription = "Setup profile"
      )
    }
  }
}
