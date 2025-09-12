package com.zcf.composeexercise.widget

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.exp
import kotlin.math.pow

/**
 * Reference:
 * https://medium.com/@kappdev/how-to-create-a-jumpy-row-layout-in-jetpack-compose-45571835f4b2
 * https://gist.github.com/L10n42/a24ef2310213332c6aa1d6371e4a7a24
 *
 * A composable that arranges content in a row with a wave-like animation effect.
 * The wave effect makes the content appear to jump up and down in a smooth, repeating motion.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param waveWidth The width of the wave effect, measured in Dp.
 * @param waveHeight The height of the wave effect, measured in Dp.
 * @param animationSpec Defines the animation specifications that control the animation behavior.
 * @param content The content to be displayed inside the row.
 */
@Composable
fun JumpyRow(
  modifier: Modifier = Modifier,
  waveWidth: Dp = 200.dp,
  waveHeight: Dp = 25.dp,
  animationSpec: InfiniteRepeatableSpec<Float> = infiniteRepeatable(
    animation = tween(2000, easing = LinearEasing),
    repeatMode = RepeatMode.Restart
  ),
  content: @Composable () -> Unit
) {
  val infiniteTransition = rememberInfiniteTransition("Wave Transition")
  val waveProgress by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = animationSpec,
    label = "Wave Progress"
  )

  Layout(
    modifier = modifier,
    content = content
  ) { measurables, constraints ->
    val waveWidthPx = waveWidth.roundToPx()
    val waveHeightPx = waveHeight.roundToPx()

    val placeables = measurables.map { measurable ->
      measurable.measure(constraints)
    }

    val rowWidth = placeables.sumOf { it.width }
    val maxHeight = placeables.maxOf { it.height }
    val rowHeight = maxHeight + waveHeightPx

    layout(width = rowWidth, height = rowHeight) {
      var xPosition = 0
      val totalDistance = rowWidth + waveWidthPx
      val waveStart = -waveWidthPx + (totalDistance * waveProgress)
      val waveEnd = waveStart + waveWidthPx

      placeables.forEach { placeable ->

        val itemCenterX = xPosition + (placeable.width / 2f)
        val baseYPosition = rowHeight - placeable.height

        val yPosition = if (itemCenterX in waveStart..waveEnd) {
          val normalizedX = normalizeX(itemCenterX, waveStart, waveEnd, -2f, 2f)
          val waveEffect = waveCurve(normalizedX)
          (baseYPosition - waveHeightPx * waveEffect).toInt()
        } else {
          baseYPosition
        }

        placeable.place(x = xPosition, y = yPosition)
        xPosition += placeable.width
      }
    }
  }
}

private fun normalizeX(x: Float, originalMin: Float, originalMax: Float, targetMin: Float, targetMax: Float): Float {
  return targetMin + ((x - originalMin) / (originalMax - originalMin)) * (targetMax - targetMin)
}

private fun waveCurve(x: Float): Float {
  return exp(-x.pow(2))
}
