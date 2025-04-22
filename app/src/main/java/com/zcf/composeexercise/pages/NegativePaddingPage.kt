package com.zcf.composeexercise.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset

/**
 * Reference:
 * https://medium.com/mobile-app-development-publication/negative-padding-for-jetpack-compose-made-possible-c82283d15564
 * https://github.com/elye/demo_android_jetpack_compose_custom_layout/blob/main/app/src/main/java/com/example/customlayout/NegativePaddingExperimentActivity.kt
 */
@Composable
fun NegativePaddingsPage() {
  Scaffold(
    modifier = Modifier.padding(horizontal = 20.dp).border(width = 1.dp, color = Color.Black)
  ) { paddingValues ->
    Column(
      modifier = Modifier.padding(paddingValues),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      val textWidth = 180.dp
      var parentWidth by remember { mutableStateOf(300) }
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Parent Width: $parentWidth", modifier = Modifier.width(textWidth))
        Slider(
          value = parentWidth.toFloat(),
          onValueChange = { parentWidth = it.toInt() },
          valueRange = 0f..300f
        )
      }
      var parentPadding by remember { mutableStateOf(0) }
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Parent Padding: $parentPadding", modifier = Modifier.width(textWidth))
        Slider(
          value = parentPadding.toFloat(),
          onValueChange = { parentPadding = it.toInt() },
          valueRange = 0f..64f
        )
      }
      var dividersPadding by remember { mutableStateOf(0) }
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "Dividers' Padding: $dividersPadding",
          modifier = Modifier.width(textWidth)
        )
        Slider(
          value = dividersPadding.toFloat(),
          onValueChange = { dividersPadding = it.toInt() },
          valueRange = -150f..150f
        )
      }
      Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
          .width(parentWidth.dp)
          .background(Color.Yellow)
          .padding(parentPadding.dp)
      ) {
        Text("0. Reference Divider")
        Divider(modifier = Modifier.height(20.dp))
        Text("1. Offset Padding")
        DividerOffset(dividersPadding)
        Text("2. Required Width Padding")
        DividerRequiredWidth(parentWidth, parentPadding, dividersPadding)
        Text("3. Layout Constraint Only Padding")
        DividerLayoutConstraint(dividersPadding)
        Text("4. Layout Managed Padding")
        DividerLayoutManaged(dividersPadding)

        Spacer(modifier = Modifier.height(30.dp))
        Text("My Negative Padding Widget")
        MyNegativePaddingWidget(10.dp)
      }
    }
  }
}

@Composable
private fun DividerLayoutManaged(dividersPadding: Int) {
  Divider(
    modifier = Modifier
      .height(20.dp)
      .layout { measurable, constraints ->
        val placeable =
          measurable.measure(constraints.offset((-dividersPadding * 2).dp.roundToPx()))
        layout(
          placeable.width + (dividersPadding * 2).dp.roundToPx(),
          placeable.height
        ) { placeable.place(0 + dividersPadding.dp.roundToPx(), 0) }
      }
  )
}

@Composable
private fun DividerLayoutConstraint(dividersPadding: Int) {
  Divider(
    modifier = Modifier
      .height(20.dp)
      .layout { measurable, constraints ->
        val placeable =
          measurable.measure(constraints.offset((-dividersPadding * 2).dp.roundToPx()))
        layout(
          placeable.width,
          placeable.height
        ) { placeable.place(0, 0) }
      }
  )
}

@Composable
private fun DividerRequiredWidth(
  parentWidth: Int,
  parentPadding: Int,
  dividersPadding: Int
) {
  Divider(
    modifier = Modifier
      .height(20.dp)
      .requiredWidth(
        width = (parentWidth.dp - parentPadding.dp * 2 - dividersPadding.dp * 2)
      )
  )
}

@Composable
private fun DividerOffset(dividersPadding: Int) {
  Divider(
    modifier = Modifier
      .height((20.dp))
      .offset(dividersPadding.dp)
  )
}

@Composable
private fun MyNegativePaddingWidget(paddingDp: Dp) {
  val padding = with(LocalDensity.current) { paddingDp.roundToPx() }
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(100.dp)
      .background(Color.Red)
      .layout { measurable, constraints ->
        val placeable = measurable.measure(constraints.offset(padding * 2))
        layout(
          width = placeable.width + padding * 4,
          height = placeable.height
        ) {
          placeable.place(padding * 4, 0)
        }
      }
      .height(50.dp)
      .background(Color.Green)
  ) {
    Text(text = "Content", color = Color.White)
  }
}
