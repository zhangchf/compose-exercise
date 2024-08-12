package com.zcf.composeexercise.widget

import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.zcf.composeexercise.ui.theme.Colors

/**
 * Reference 1: https://www.sinasamaki.com/custom-dialog-animation-in-jetpack-compose/
 *      gist: https://gist.github.com/sinasamaki/daa825d96235a18822177a2b1b323f49?ref=sinasamaki.com
 *
 * Reference 2: https://proandroiddev.com/how-to-master-swipeable-and-nestedscroll-modifiers-in-compose-bb0635d6a760
 *      gist: https://gist.github.com/arcadefire/7fe138c0ded1a36bee6dd57acdfa3d18
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AliasBottomSheetDialog(
  showDialog: Boolean,
  onDismissRequest: () -> Unit,
  isCancellable: Boolean = true,
  enter: EnterTransition = slideInVertically(tween(DIALOG_ANIM_DURATION)) { it } + fadeIn(),
  exit: ExitTransition = slideOutVertically(tween(DIALOG_ANIM_DURATION)) { it } + fadeOut(),
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
        dismissOnClickOutside = isCancellable,
        usePlatformDefaultWidth = false
      )
    ) {
      findDialogWindowProviderInParent(LocalView.current)?.window?.let { window ->
        window.setWindowAnimations(-1)
        window.setDimAmount(0f)
      }

      // this value is not important, but has to be different with the Expanded offset 0f.
      val estimatedTotalHeight = 200f
      var totalHeight by remember { mutableFloatStateOf(estimatedTotalHeight) }
      val swipeableState = rememberSwipeableState(initialValue = BottomSheetState.Expanded) { state ->
        if (state == BottomSheetState.Collapsed) {
          onDismissRequest()
        }
        true
      }
      val innerScrollState = rememberScrollState()

      val nestedConnection = remember {
        BottomSheetNestedScrollConnection(
          swipeableState = swipeableState,
          innerScrollableReachedTop = {
            innerScrollState.value == 0
          }
        )
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
            .background(Colors.dimBackground)
            .fillMaxSize()
            .then(
              if (isCancellable) {
                Modifier
                  .pointerInput(Unit) {
                    detectTapGestures { onDismissRequest() }
                  }
                  .pointerInput(Unit) {
                    detectDragGestures { _, _ -> }
                  }
              } else {
                Modifier
              }
            )
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
          val roundedShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
          Box(
            modifier = Modifier
              .then(
                if (isCancellable) {
                  Modifier
                    .onGloballyPositioned {
                      totalHeight = it.size.height.toFloat()
                    }
                    .offset {
                      IntOffset(
                        0,
                        swipeableState.offset.value.toInt()
                      )
                    }
                    .swipeable(
                      state = swipeableState,
                      anchors = mapOf(
                        0f to BottomSheetState.Expanded,
                        totalHeight + 100f to BottomSheetState.Collapsed
                      ),
                      orientation = Orientation.Vertical,
                      thresholds = { _, _ -> FractionalThreshold(0.5f) }
                    )
                    .nestedScroll(nestedConnection)
                } else {
                  Modifier
                }
              )
              .fillMaxWidth()
              .clip(roundedShape)
              .background(Colors.windowBackground),
            contentAlignment = Alignment.BottomCenter
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(innerScrollState),
              contentAlignment = Alignment.BottomCenter
            ) {
              content()
            }
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
