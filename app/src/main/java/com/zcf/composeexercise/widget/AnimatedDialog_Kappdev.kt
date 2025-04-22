package com.zcf.composeexercise.widget

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Reference: https://medium.com/@kappdev/crafting-custom-3d-dialog-animation-in-jetpack-compose-b4038f7888d5
 * Gist: https://gist.github.com/L10n42/2815f5c5dde03b4e49b962d337b414ae
 * A composable function that opens a dialog with a custom 3D animation and the given content.
 *
 * @param onDismiss A callback function to be executed when the dialog is dismissed.
 * @param inAnimDuration The duration (in milliseconds) of the dialog's entry animation.
 * @param outAnimDuration The duration (in milliseconds) of the dialog's exit animation.
 * @param properties [DialogProperties] for further customization of this dialog's behavior.
 * @param content The content to be displayed inside the dialog.
 */
@Composable
fun AnimatedDialog(
  onDismiss: () -> Unit,
  inAnimDuration: Int = 720,
  outAnimDuration: Int = 450,
  properties: DialogProperties = DialogProperties(),
  content: @Composable (triggerDismiss: () -> Unit) -> Unit
) {
  val scope = rememberCoroutineScope()
  var isDialogVisible by remember { mutableStateOf(false) }
  val animationSpec = tween<Float>(if (isDialogVisible) inAnimDuration else outAnimDuration)

  val dialogAlpha by animateFloatAsState(
    targetValue = if (isDialogVisible) 1f else 0f,
    animationSpec = animationSpec
  )

  val dialogRotationX by animateFloatAsState(
    targetValue = if (isDialogVisible) 0f else -90f,
    animationSpec = animationSpec
  )

  val dialogScale by animateFloatAsState(
    targetValue = if (isDialogVisible) 1f else 0f,
    animationSpec = animationSpec
  )

  val dismissWithAnimation: () -> Unit = {
    scope.launch {
      isDialogVisible = false
      delay(outAnimDuration.toLong())
      onDismiss()
    }
  }

  LaunchedEffect(Unit) {
    isDialogVisible = true
  }

  Dialog(
    onDismissRequest = dismissWithAnimation,
    properties = properties
  ) {
    Box(
      modifier = Modifier
        .alpha(dialogAlpha)
        .scale(dialogScale)
        .graphicsLayer { rotationX = dialogRotationX },
      content = {
        content(dismissWithAnimation)
      }
    )
  }
}
