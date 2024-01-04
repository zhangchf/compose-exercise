package com.zcf.composeexercise.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.zcf.composeexercise.ui.theme.Colors

/**
 * Reference 1: https://www.sinasamaki.com/custom-dialog-animation-in-jetpack-compose/
 *      gist: https://gist.github.com/sinasamaki/daa825d96235a18822177a2b1b323f49?ref=sinasamaki.com
 *
 * Reference 2: https://proandroiddev.com/how-to-master-swipeable-and-nestedscroll-modifiers-in-compose-bb0635d6a760
 *      gist: https://gist.github.com/arcadefire/7fe138c0ded1a36bee6dd57acdfa3d18
 */
@Composable
fun AliasFullScreenDialog(
  showDialog: Boolean,
  onDismissRequest: () -> Unit,
  isCancellable: Boolean = true,
  enter: EnterTransition =
    slideInVertically(tween(DIALOG_ANIM_DURATION)) { it } + fadeIn(tween(DIALOG_ANIM_DURATION)),
  exit: ExitTransition =
    slideOutVertically(tween(DIALOG_ANIM_DURATION)) { it } + fadeOut(tween(DIALOG_ANIM_DURATION)),
  content: @Composable () -> Unit
) {
  var showAnimatedDialog by remember { mutableStateOf(false) }

  LaunchedEffect(showDialog) {
    if (showDialog) showAnimatedDialog = true
  }

  if (showAnimatedDialog) {
    Dialog(
      onDismissRequest = onDismissRequest,
      properties = DialogProperties(
        dismissOnBackPress = isCancellable,
        dismissOnClickOutside = false,
        usePlatformDefaultWidth = false
      )
    ) {
      findDialogWindowProviderInParent(LocalView.current)?.window?.let { window ->
        window.setWindowAnimations(-1)
        window.setDimAmount(0f)
      }

      var animateIn by remember { mutableStateOf(false) }
      LaunchedEffect(Unit) { animateIn = true }

      AnimatedVisibility(
        visible = animateIn && showDialog,
        enter = fadeIn(),
        exit = fadeOut()
      ) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(Colors.dimBackground)
        )
      }

      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
      ) {
        AnimatedVisibility(
          visible = animateIn && showDialog,
          enter = enter,
          exit = exit
        ) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(Colors.windowBackground),
            contentAlignment = Alignment.BottomCenter
          ) {
            content()
          }

          DisposableEffect(key1 = Unit) {
            onDispose {
              showAnimatedDialog = false
            }
          }
        }
      }
    }
  }
}
