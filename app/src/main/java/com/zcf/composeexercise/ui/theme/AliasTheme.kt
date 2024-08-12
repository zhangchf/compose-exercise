package com.zcf.composeexercise.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AliasTheme(content: @Composable () -> Unit) {
  MaterialTheme(
    colors = Colors.materialColors,
    typography = TextStyles.materialTypography,
    shapes = Shapes,
    content = content
  )
}
