package com.zcf.composeexercise.widget

import android.view.ActionMode
import android.view.View
import androidx.annotation.DoNotInline
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus

// To disable actions, like Copy/Paste/Select/Cut, when TextField is long pressed
object EmptyTextToolbar : TextToolbar {
  override val status: TextToolbarStatus = TextToolbarStatus.Hidden

  override fun hide() {
  }

  override fun showMenu(
    rect: Rect,
    onCopyRequested: (() -> Unit)?,
    onPasteRequested: (() -> Unit)?,
    onCutRequested: (() -> Unit)?,
    onSelectAllRequested: (() -> Unit)?
  ) {
  }
}

/**
 * To enable only Paste action, when TextField is long pressed
 *
 * Inspired by [androidx.compose.ui.platform.AndroidTextToolbar]
 */
class PasteOnlyTextToolbar(private val view: View) : TextToolbar {
  private var actionMode: ActionMode? = null
  private val pasteOnlyTextActionModeCallback: PasteOnlyTextActionModeCallback =
    PasteOnlyTextActionModeCallback(
      onActionModeDestroy = {
        actionMode = null
      }
    )
  override var status: TextToolbarStatus = TextToolbarStatus.Hidden
    private set

  override fun showMenu(
    rect: Rect,
    onCopyRequested: (() -> Unit)?,
    onPasteRequested: (() -> Unit)?,
    onCutRequested: (() -> Unit)?,
    onSelectAllRequested: (() -> Unit)?
  ) {
    pasteOnlyTextActionModeCallback.rect = rect
    pasteOnlyTextActionModeCallback.onPasteRequested = onPasteRequested
    if (actionMode == null) {
      status = TextToolbarStatus.Shown
      actionMode =
        TextToolbarHelperMethods.startActionMode(
          view,
          FloatingPasteOnlyTextActionModeCallback(pasteOnlyTextActionModeCallback),
          ActionMode.TYPE_FLOATING
        )
    } else {
      actionMode?.invalidate()
    }
  }

  override fun hide() {
    status = TextToolbarStatus.Hidden
    actionMode?.finish()
    actionMode = null
  }
}

internal object TextToolbarHelperMethods {
  @DoNotInline
  fun startActionMode(
    view: View,
    actionModeCallback: ActionMode.Callback,
    type: Int
  ): ActionMode? {
    return view.startActionMode(
      actionModeCallback,
      type
    )
  }

  @DoNotInline
  fun invalidateContentRect(actionMode: ActionMode) {
    actionMode.invalidateContentRect()
  }
}
