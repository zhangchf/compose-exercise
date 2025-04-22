package com.zcf.composeexercise.pages

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zcf.composeexercise.R
import com.zcf.composeexercise.widget.AnimatedStrikethroughText
import kotlin.math.sin

data class Star(
  var x: Float,
  var y: Float,
  var alpha: Float
) {
  private val initialAlpha = alpha

  fun update(value: Float) {
    val x = value - initialAlpha
    alpha = (0.5 + 0.5 * sin(x)).toFloat()
  }
}

@Composable
fun AnimationPage() {
  Column {
    Space()
    BrushColorAnimatingText()
    AnimatedStrikeThroughTextDemo()
  }
}

@Composable
fun Space(
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "")
  val infiniteAnimatedFloat = infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 2f * Math.PI.toFloat(),
    animationSpec = infiniteRepeatable(
      tween(10000),
      repeatMode = RepeatMode.Restart
    ),
    label = ""
  )

  BoxWithConstraints(
    modifier = modifier
      .width(200.dp)
      .aspectRatio(1f)
  ) {
    val density = LocalDensity.current
    val width = with(density) { maxWidth.toPx() }
    val height = with(density) { maxHeight.toPx() }

    val stars = remember {
      buildList {
        repeat(1000) {
          val x = Math.random() * width
          val y = Math.random() * height
          val alpha = Math.random() * 2 * Math.PI
          add(Star(x.toFloat(), y.toFloat(), alpha.toFloat()))
        }
      }
    }

    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
    ) {
      stars.forEach { star ->
        star.update(infiniteAnimatedFloat.value)
        drawCircle(
          color = Color.White,
          center = Offset(star.x, star.y),
          radius = 2f,
          alpha = star.alpha
        )
      }
    }
  }
}

@Composable
fun BrushColorAnimatingText() {
  val fontSize = 14.sp
  val currentFontSizePx = with(LocalDensity.current) { fontSize.toPx() }
  val currentFontSizeDoublePx = currentFontSizePx * 2

  val infiniteTransition = rememberInfiniteTransition()
  val offset by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = currentFontSizeDoublePx,
    animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing)),
    label = ""
  )

  val brush = Brush.linearGradient(
    colors = listOf(Color.Blue, Color.Magenta),
    start = Offset(offset, offset),
    end = Offset(offset + currentFontSizePx, offset + currentFontSizePx),
    tileMode = TileMode.Mirror
  )

  Text(
    text = stringResource(id = R.string.loremIpsum),
    style = TextStyle(
      brush = brush,
      fontSize = fontSize
    )
  )
}

@Composable
fun AnimatedStrikeThroughTextDemo() {
  val tasks = listOf(
    "Grab a coffee",
    "Find a comfy spot",
    "Write some code"
  )

  Column {
    tasks.forEach { task ->
      var checked by remember { mutableStateOf(false) }

      TodoItem(
        text = task,
        checked = checked,
        onCheckedChange = {
          checked = it
        }
      )
    }
  }
}

@Composable
fun TodoItem(
  text: String,
  checked: Boolean,
  modifier: Modifier = Modifier,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Checkbox(
      checked = checked,
      onCheckedChange = onCheckedChange
    )

    AnimatedStrikethroughText(
      text = text,
      isVisible = checked,
      strikethroughStyle = SpanStyle(
        color = Color.Black.copy(0.32f)
      ),
      textStyle = LocalTextStyle.current.copy(
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium
      )
    )
  }
}
