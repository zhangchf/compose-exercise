package com.zcf.composeexercise.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zcf.composeexercise.R
import kotlin.math.min

@Composable
fun ParallaxPage() {
  val scrollState = rememberScrollState()

  val threshold = with(LocalDensity.current) {
    200.dp.toPx()
  }

  Box() {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(scrollState)
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(500.dp)
          .background(Color.White)
          .graphicsLayer {
            val scroll = scrollState.value.toFloat()
            alpha = 0.5f + (0.5f - min(scroll, threshold) / threshold / 2)
            translationY = scroll / 2f
          }
      ) {
        Image(
          painter = painterResource(id = R.drawable.sky_sea_mountain),
          contentDescription = null,
          alignment = Alignment.Center,
          contentScale = ContentScale.Crop
        )
      }
      Text(
        text = stringResource(id = R.string.dummy_text_long),
        modifier = Modifier.background(Color.White)
      )
      println("zcf ${stringResource(id = R.string.dummy_text_long)}")
    }

    Box(
      modifier = Modifier
        .alpha(min(1f, scrollState.value / threshold))
        .fillMaxWidth()
        .height(60.dp)
        .background(Color.Yellow),
      contentAlignment = Alignment.CenterStart
    ) {
      Text(
        text = "Header Bar",
        modifier = Modifier.padding(horizontal = 16.dp),
        style = TextStyle(
          fontSize = 24.sp,
          fontWeight = FontWeight.W900
        )
      )
    }
  }
}
