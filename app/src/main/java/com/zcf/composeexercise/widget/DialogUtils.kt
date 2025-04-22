package com.zcf.composeexercise.widget

import android.view.View
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.window.DialogWindowProvider

const val DIALOG_ANIM_DURATION = 400

/**
 * Reference: [findDialogWindowProviderInParent] method in [AndroidImageHelpers.android.kt]
 */
fun findDialogWindowProviderInParent(view: View): DialogWindowProvider? {
  if (view is DialogWindowProvider) {
    return view
  }
  val parent = view.parent ?: return null
  if (parent is View) {
    return findDialogWindowProviderInParent(parent)
  }
  return null
}

enum class BottomSheetState {
  Expanded, Collapsed
}

@OptIn(ExperimentalMaterialApi::class)
class BottomSheetNestedScrollConnection(
  val swipeableState: SwipeableState<BottomSheetState>,
  val innerScrollableReachedTop: () -> Boolean
) : NestedScrollConnection {
  override fun onPreScroll(
    available: Offset,
    source: NestedScrollSource
  ): Offset {
    val delta = available.y
    return if (delta < 0) {
      swipeableState.performDrag(delta).toOffset()
    } else {
      Offset.Zero
    }
  }

  override fun onPostScroll(
    consumed: Offset,
    available: Offset,
    source: NestedScrollSource
  ): Offset {
    val delta = available.y
    return swipeableState.performDrag(delta).toOffset()
  }

  override suspend fun onPreFling(available: Velocity): Velocity {
    return if (available.y < 0 && innerScrollableReachedTop()) {
      swipeableState.performFling(available.y)
      available
    } else {
      Velocity.Zero
    }
  }

  override suspend fun onPostFling(
    consumed: Velocity,
    available: Velocity
  ): Velocity {
    swipeableState.performFling(velocity = available.y)
    return available
  }

  private fun Float.toOffset() = Offset(0f, this)
}
