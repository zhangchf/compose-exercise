package com.zcf.composeexercise.pages

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus
import androidx.compose.ui.unit.dp

@Composable
fun TextToolbarPage() {
    Column {
        Spacer(modifier = Modifier.height(100.dp))
        CompositionLocalProvider {
            var text by remember {
                mutableStateOf("")
            }
            TextField(value = text, onValueChange = { text = it })
        }
    }
}

private class CopyTextToolbar(private val view: View) : TextToolbar {
    private val actionModeCallback: ActionMode.Callback = object: ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            TODO("Not yet implemented")
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            TODO("Not yet implemented")
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            TODO("Not yet implemented")
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            TODO("Not yet implemented")
        }

    }

    override val status: TextToolbarStatus
        get() = TODO("Not yet implemented")

    override fun hide() {
        TODO("Not yet implemented")
    }

    override fun showMenu(
        rect: Rect,
        onCopyRequested: (() -> Unit)?,
        onPasteRequested: (() -> Unit)?,
        onCutRequested: (() -> Unit)?,
        onSelectAllRequested: (() -> Unit)?
    ) {
        view.startActionMode(actionModeCallback, ActionMode.TYPE_FLOATING)
    }

}