package com.zcf.composeexercise.widget

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.compose.ui.geometry.Rect

/**
 * Inspired by
 * [androidx.compose.ui.platform.actionmodecallback.TextActionModeCallback]
 */
internal class PasteOnlyTextActionModeCallback(
  val onActionModeDestroy: (() -> Unit)? = null,
  var rect: Rect = Rect.Zero,
  var onPasteRequested: (() -> Unit)? = null
) {
  fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
    requireNotNull(menu)
    requireNotNull(mode)

    onPasteRequested?.let {
      addMenuItem(menu, MenuItemOption.Paste)
    }
    return true
  }

  fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
    if (mode == null || menu == null) return false
    updateMenuItems(menu)
    return true
  }

  fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
    when (item!!.itemId) {
      MenuItemOption.Paste.id -> onPasteRequested?.invoke()
      else -> return false
    }
    mode?.finish()
    return true
  }

  fun onDestroyActionMode() {
    onActionModeDestroy?.invoke()
  }

  private fun updateMenuItems(menu: Menu) {
    addOrRemoveMenuItem(menu, MenuItemOption.Paste, onPasteRequested)
  }

  private fun addMenuItem(menu: Menu, item: MenuItemOption) {
    menu.add(0, item.id, item.order, item.titleResource)
      .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
  }

  private fun addOrRemoveMenuItem(
    menu: Menu,
    item: MenuItemOption,
    callback: (() -> Unit)?
  ) {
    when {
      callback != null && menu.findItem(item.id) == null -> addMenuItem(menu, item)
      callback == null && menu.findItem(item.id) != null -> menu.removeItem(item.id)
    }
  }
}

internal enum class MenuItemOption(val id: Int) {
  Paste(0);

  val titleResource: Int
    get() = when (this) {
      Paste -> android.R.string.paste
    }

  val order = id
}

/**
 * Inspired by
 * [androidx.compose.ui.platform.actionmodecallback.FloatingTextActionModeCallback]
 */
internal class FloatingPasteOnlyTextActionModeCallback(
  private val callback: PasteOnlyTextActionModeCallback
) : ActionMode.Callback2() {
  override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
    return callback.onActionItemClicked(mode, item)
  }

  override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
    return callback.onCreateActionMode(mode, menu)
  }

  override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
    return callback.onPrepareActionMode(mode, menu)
  }

  override fun onDestroyActionMode(mode: ActionMode?) {
    callback.onDestroyActionMode()
  }

  override fun onGetContentRect(
    mode: ActionMode?,
    view: View?,
    outRect: android.graphics.Rect?
  ) {
    val rect = callback.rect
    outRect?.set(
      rect.left.toInt(),
      rect.top.toInt(),
      rect.right.toInt(),
      rect.bottom.toInt()
    )
  }
}
