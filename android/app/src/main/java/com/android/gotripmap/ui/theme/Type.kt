package com.android.gotripmap.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.android.gotripmap.R

val arialFamily = FontFamily(
  Font(R.font.arial_regular, FontWeight.Normal),
  Font(R.font.arial_italic, FontWeight.Normal, FontStyle.Italic),
  Font(R.font.arial_bold_italic, FontWeight.Bold, FontStyle.Italic),
  Font(R.font.arial_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
val Typography = Typography(
  bodyLarge = TextStyle(
    fontFamily = arialFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 14.4.sp,
    letterSpacing = 0.5.sp
  ),
    bodyMedium = TextStyle(
        fontFamily = arialFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 14.4.sp,
        letterSpacing = 0.5.sp
    ),
    bodySmall= TextStyle(
      fontFamily = arialFamily,
      fontWeight = FontWeight.Normal,
      fontSize = 12.sp,
      lineHeight = 14.4.sp,
      letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
      fontFamily = arialFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 14.sp,
      lineHeight = 14.4.sp,
      letterSpacing = 0.5.sp
    ),
  titleSmall = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 16.sp,
    letterSpacing = 1.25.sp
  ),
  titleMedium = TextStyle(
    fontFamily = arialFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 20.sp,
    lineHeight = 14.4.sp,
    letterSpacing = 0.5.sp
  ),
  titleLarge = TextStyle(
    fontFamily = arialFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 24.sp,
    lineHeight = 14.4.sp,
    letterSpacing = 0.5.sp
  )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

