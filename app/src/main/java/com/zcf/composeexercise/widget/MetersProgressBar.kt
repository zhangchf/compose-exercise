package com.zcf.composeexercise.widget

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zcf.composeexercise.ui.theme.AliasTheme
import com.zcf.composeexercise.ui.theme.Colors
import kotlin.math.roundToInt

val METERS_PROGRESS_BAR_DEFAULT_HEIGHT = 52.dp

/**
 * @param modifier Define ProgressBar's width and height with the modifier.
 * E.g. Modifier.fillMaxWidth().height(METERS_PROGRESS_BAR_DEFAULT_HEIGHT)
 * @param progress in [0f .. 1f]
 */
@Composable
fun MetersProgressBar(
  modifier: Modifier = Modifier
    .fillMaxWidth()
    .height(METERS_PROGRESS_BAR_DEFAULT_HEIGHT),
  progress: Float = 0.0f,
  totalBars: Int = 20,
  activeColor: Color = Colors.links,
  inactiveColor: Color = Colors.gray3Solid
) {
  Canvas(modifier = modifier.fillMaxSize()) {
    val totalGaps = totalBars - 1
    val gapWidth = size.width / (totalBars * BAR_AND_GAP_WIDTH_RATIO + totalGaps)
    val barWidth = (size.width - gapWidth * totalGaps) / totalBars
    val activeProgress = (progress.coerceIn(0.0f, 1.0f) * totalBars).roundToInt()
    (0 until totalBars).forEach { index ->
      drawRect(
        color = if (index <= activeProgress) activeColor else inactiveColor,
        topLeft = Offset(x = index * (barWidth + gapWidth), y = 0f),
        size = Size(width = barWidth, height = size.height)
      )
    }
  }
}

/**
 * @param modifier Define ProgressBar's width and height with the modifier.
 * E.g. Modifier.fillMaxWidth().height(48.dp)
 * @param progress in [0f .. 1f]
 */
@Composable
fun BoxedMetersProgressBar(
  modifier: Modifier = Modifier,
  progress: Float = 0.0f,
  totalBars: Int = 20,
  activeColor: Color = Colors.links,
  inactiveColor: Color = Colors.gray3Solid
) {
  Box(
    modifier = modifier
      .border(width = 1.dp, color = Colors.darkGray)
      .padding(horizontal = 12.dp, vertical = 8.dp)
  ) {
    MetersProgressBar(
      modifier = Modifier.fillMaxSize(),
      progress = progress,
      totalBars = totalBars,
      activeColor = activeColor,
      inactiveColor = inactiveColor
    )
  }
}

@Preview
@Composable
private fun BoxedMetersProgressBarPreview() {
  AliasTheme {
    BoxedMetersProgressBar(
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp),
      progress = 3.0f,
      inactiveColor = Color.Transparent
    )
  }
}

private const val BAR_AND_GAP_WIDTH_RATIO = 10 / 7f
