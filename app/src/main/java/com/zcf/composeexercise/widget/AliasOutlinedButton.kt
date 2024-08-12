package com.zcf.composeexercise.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zcf.composeexercise.ui.theme.Colors
import com.zcf.composeexercise.ui.theme.TextStyles

@Composable
fun AliasOutlinedButton(
  title: String,
  modifier: Modifier = Modifier,
  isEnabled: Boolean = true,
  isHighlighted: Boolean = false,
  isOutlined: Boolean = true,
  onClick: () -> Unit
) {
  val (borderColor, backgroundColor, contentColor) = if (isEnabled) {
    if (isHighlighted) {
      Triple(Colors.primary, Colors.primary, Colors.gray3Solid)
    } else {
      Triple(Colors.gray1Solid, Colors.darkGray, Colors.gray3Solid)
    }
  } else {
    Triple(Colors.darkGray, Colors.background, Colors.gray1Solid)
  }
  AliasOutlinedButton(
    title = title,
    modifier = modifier,
    isEnabled = isEnabled,
    borderColor = if (isOutlined) borderColor else Color.Unspecified,
    backgroundColor = backgroundColor,
    contentColor = contentColor,
    onClick = onClick
  )
}

@Composable
fun AliasOutlinedButton(
  title: String,
  modifier: Modifier = Modifier,
  isEnabled: Boolean = true,
  borderColor: Color = Colors.gray1Solid,
  backgroundColor: Color = Colors.darkGray,
  contentColor: Color = Colors.gray3Solid,
  onClick: () -> Unit
) {
  AliasOutlinedButton(
    onClick = onClick,
    modifier = modifier,
    isEnabled = isEnabled,
    borderColor = borderColor,
    backgroundColor = backgroundColor,
    contentColor = contentColor
  ) {
    Text(
      text = title,
      style = TextStyles.h2Medium()
    )
  }
}

@Composable
fun AliasOutlinedButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  isEnabled: Boolean = true,
  borderColor: Color = Colors.gray1Solid,
  backgroundColor: Color = Colors.darkGray,
  contentColor: Color = Colors.gray3Solid,
  content: @Composable () -> Unit
) {
  Box(
    modifier = modifier
      .then(
        if (borderColor != Color.Unspecified) {
          Modifier.border(width = 1.dp, color = borderColor)
        } else {
          Modifier
        }
      )
      .fillMaxWidth()
      .height(56.dp)
      .background(color = backgroundColor)
      .clickable { if (isEnabled) onClick() },
    contentAlignment = Alignment.Center
  ) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
      content()
    }
  }
}
