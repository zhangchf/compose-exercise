package com.zcf.composeexercise.pages

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zcf.composeexercise.widget.ANIMATION_DURATION
import com.zcf.composeexercise.widget.AliasModalBottomSheet
import com.zcf.composeexercise.widget.AnimatedTransitionDialog
import com.zcf.composeexercise.widget.AnimatedTransitionDialogWithCallback
import com.zcf.composeexercise.widget.CustomAnimationDialog
import com.zcf.composeexercise.widget.CustomAnimationDialog_BottomSheet
import com.zcf.composeexercise.widget.CustomAnimationDialog_Draggable
import kotlinx.coroutines.launch

@Composable
fun AnimatedBottomSheetDialogPage() {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showAnimatedTransitionDialog by remember { mutableStateOf(false) }
    var showAnimatedTransitionDialogWithCallback by remember { mutableStateOf(false) }
    var showCustomAnimationDialog by remember { mutableStateOf(false) }
    var showCustomAnimationDialogBottomSheet by remember { mutableStateOf(false) }
    var showCustomAnimationDialogDraggable by remember { mutableStateOf(false) }
    var showAliasModalBottomSheet by remember { mutableStateOf(false) }
    var showAliasModalBottomSheetDraggable by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = {
            showAnimatedTransitionDialog = true
        }) {
            Text(text = "Show animated transition dialog")
        }

        Button(onClick = {
            showAnimatedTransitionDialogWithCallback = true
        }) {
            Text(text = "Show animated transition dialog with callback")
        }

        Button(onClick = {
            showCustomAnimationDialog = true
        }) {
            Text(text = "Custom animation dialog")
        }

        Button(onClick = {
            showCustomAnimationDialogBottomSheet = true
        }) {
            Text(text = "Custom animation dialog (Simplified bottom sheet)")
        }

        Button(onClick = {
            showCustomAnimationDialogDraggable = true
        }) {
            Text(text = "Custom animation dialog (Draggable)")
        }

        Button(onClick = {
            showAliasModalBottomSheet = true
        }) {
            Text(text = "Alias ModalBottomSheet")
        }

        Button(onClick = {
            showAliasModalBottomSheetDraggable = true
        }) {
            Text(text = "Alias ModalBottomSheet (Draggable)")
        }

        Spacer(modifier = Modifier.weight(1f))
        SnackbarHost(
            hostState = snackbarHostState
        )
    }

    fun onPositiveButton() {
        scope.launch {
            println("zcf positive button clicked")
            snackbarHostState.showSnackbar("Positive button clicked")
        }
    }

    fun onNegativeButton() {
        scope.launch {
            snackbarHostState.showSnackbar("Negative button clicked")
        }
    }

    if (showAnimatedTransitionDialog) {
        AnimatedTransitionDialog(
            onDismissRequest = {
                showAnimatedTransitionDialog = false
            },
            enter = slideInVertically(animationSpec = tween(ANIMATION_DURATION)) { it },
            exit = slideOutVertically(animationSpec = tween(ANIMATION_DURATION)) { it },
            animationDuration = ANIMATION_DURATION
        ) { animatedTransitionDialogHelper ->
            DialogContent(
                onPositiveButton = {
                    animatedTransitionDialogHelper.triggerAnimatedDismiss()
                    onPositiveButton()
                },
                onNegativeButton = {
                    animatedTransitionDialogHelper.triggerAnimatedDismiss()
                    onNegativeButton()
                }
            )
        }
    }

    if (showAnimatedTransitionDialogWithCallback) {
        AnimatedTransitionDialogWithCallback(
            onDismissRequest = {
                showAnimatedTransitionDialogWithCallback = false
            },
            enter = slideInVertically(animationSpec = tween(ANIMATION_DURATION)) { it },
            exit = slideOutVertically(animationSpec = tween(ANIMATION_DURATION)) { it },
            animationDuration = ANIMATION_DURATION
        ) { dismissCallback ->
            DialogContent(
                onPositiveButton = {
                    scope.launch { dismissCallback() }
                        .invokeOnCompletion { onPositiveButton() }
                },
                onNegativeButton = {
                    scope.launch { dismissCallback() }
                        .invokeOnCompletion { onNegativeButton() }
                }
            )
        }
    }

    CustomAnimationDialog(
        showDialog = showCustomAnimationDialog,
        onDismissRequest = { showCustomAnimationDialog = false }
    ) {
        DialogContent(
            onPositiveButton = { showCustomAnimationDialog = false },
            onNegativeButton = { showCustomAnimationDialog = false }
        )
    }

    CustomAnimationDialog_BottomSheet(
        showDialog = showCustomAnimationDialogBottomSheet,
        onDismissRequest = { showCustomAnimationDialogBottomSheet = false },
    ) {
        DialogContent(
            onPositiveButton = {
                showCustomAnimationDialogBottomSheet = false
                onPositiveButton()
            },
            onNegativeButton = {
                showCustomAnimationDialogBottomSheet = false
                onNegativeButton()
            }
        )
    }

    CustomAnimationDialog_Draggable(
        showDialog = showCustomAnimationDialogDraggable,
        onDismissRequest = { showCustomAnimationDialogDraggable = false },
    ) {
        DialogContent(
            onPositiveButton = {
                showCustomAnimationDialogDraggable = false
                onPositiveButton()
            },
            onNegativeButton = {
                showCustomAnimationDialogDraggable = false
                onNegativeButton()
            }
        )
    }

    if (showAliasModalBottomSheet) {
        AliasModalBottomSheet(
            onDismissRequest = { showAliasModalBottomSheet = false },
            isExpandedByDefault = true,
            isCancelable = false
        ) { dismissCallback ->
            DialogContent(
                onPositiveButton = {
                    scope.launch {
                        dismissCallback()
                        onPositiveButton()
                    }
                },
                onNegativeButton = {
                    scope.launch {
                        dismissCallback()
                        onNegativeButton()
                    }
                }
            )
        }
    }

    if (showAliasModalBottomSheetDraggable) {
        AliasModalBottomSheet(
            onDismissRequest = { showAliasModalBottomSheetDraggable = false },
            isExpandedByDefault = true,
            isCancelable = true
        ) { dismissCallback ->
            DialogContent(
                onPositiveButton = {
                    scope.launch {
                        dismissCallback()
                        onPositiveButton()
                    }
                },
                onNegativeButton = {
                    scope.launch {
                        dismissCallback()
                        onNegativeButton()
                    }
                }
            )
        }
    }
}

@Composable
fun DialogContent(
    onPositiveButton: () -> Unit,
    onNegativeButton: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Dialog title")

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Dialog Content")

        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            onPositiveButton()
        }) {
            Text(text = "OK")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            onNegativeButton()
        }) {
            Text(text = "Close")
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}