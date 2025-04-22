package com.zcf.composeexercise.pages

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ChessPage() {
  Box(
    modifier = Modifier.apply { }
      .fillMaxSize()
      .aspectRatio(1f)
      .border(1.dp, darkSquareColor)
      .padding(1.dp)
  ) {
    Column {
      for (rank in 8 downTo 1) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
        ) {
          for (file in 1..8) {
            Square(modifier = Modifier.weight(1f), isDark = (file + rank).isEven())
          }
        }
      }
    }
  }
}

@Composable
fun Square(modifier: Modifier, isDark: Boolean) {
  Canvas(modifier = modifier.fillMaxSize()) {
    val color = (if (isDark) darkSquareColor else lightSquareColor)
    drawRect(color)
  }
}

val darkSquareColor = Color.LightGray
val lightSquareColor = Color.White

fun Int.isEven() = this % 2 == 0

fun Color.alpha(value: Float) = this.copy(alpha = value)
