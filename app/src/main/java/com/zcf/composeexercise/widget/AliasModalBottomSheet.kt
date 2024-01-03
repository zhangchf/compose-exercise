package com.zcf.composeexercise.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitDragOrCancellation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import com.zcf.composeexercise.ui.theme.Colors
import kotlinx.coroutines.launch

/**
 * Modal BottomSheetDialog with Alias theme, slide in/out.
 *
 * Note:
 * [ModalBottomSheet] doesn't have the option to set "decorFitsSystemWindows" for now.
 * To simulate that, we have to add [navigationBarsPadding],
 * set ["scrimColor"] and ["containerColor"] to [Color.Transparent],
 * and add the upper scrim region by ourselves.
 *
 * After [ModalBottomSheet] support "decorFitsSystemWindows", we can remove those.
 *
 * @param onDismissRequest If dialog is cancellable, user should set the flag to hide the dialog here.
 * @param isExpandedByDefault If the dialog is expanded by default
 * @param isCancelable If the dialog can be cancelled. it won't be swipeable if not cancelable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AliasModalBottomSheet(
  onDismissRequest: () -> Unit,
  isExpandedByDefault: Boolean = true,
  isCancelable: Boolean = true,
  content: @Composable ColumnScope.(dismissCallback: suspend () -> Unit) -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(isExpandedByDefault) { isCancelable }
  val coroutineScope = rememberCoroutineScope()

  suspend fun dismissCallback() {
    sheetState.hide()
    onDismissRequest()
  }

  ModalBottomSheet(
    onDismissRequest = onDismissRequest,
    sheetState = sheetState,
    shape = RectangleShape,
    containerColor = Color.Transparent,
    scrimColor = Color.Transparent,
    dragHandle = {},
    properties = ModalBottomSheetDefaults.properties(shouldDismissOnBackPress = isCancelable)
  ) {
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.BottomCenter
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Colors.background80)
          .pointerInput(Unit) {
            detectTapGestures {
              if (isCancelable) {
                coroutineScope
                  .launch { sheetState.hide() }
                  .invokeOnCompletion { onDismissRequest() }
              }
            }
          }
      )
      Column(
        modifier = Modifier
          .navigationBarsPadding()
          .background(MaterialTheme.colors.surface)
          .then(
            if (!isCancelable) {
              Modifier.pointerInput(Unit) {
                awaitEachGesture {
                  val down = awaitFirstDown(requireUnconsumed = false).also { it.consume() }
                  awaitDragOrCancellation(down.id)?.consume()
                }
              }
            } else {
              Modifier
            }
          )
      ) {
        HorizontalDivider(color = Colors.darkGray)
        content(::dismissCallback)
      }
    }
  }
}
