package com.zcf.composeexercise.pages

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun AnimatedBottomSheetDialogPage() {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showAnimatedTransitionDialog by remember { mutableStateOf(false) }
    var showAnimatedTransitionDialogWithCallback by remember { mutableStateOf(false) }
    var showCustomAnimationDialog by remember { mutableStateOf(false) }
    var showCustomAnimationDialog_BottomSheet by remember { mutableStateOf(false) }

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
            showCustomAnimationDialog_BottomSheet = true
        }) {
            Text(text = "Custom animation dialog (Simplified bottom sheet)")
        }

        Spacer(modifier = Modifier.weight(1f))
        SnackbarHost(
            hostState = snackbarHostState
        )
    }

    fun onPositiveButton() {
        coroutineScope.launch {
            println("zcf positive button clicked")
            snackbarHostState.showSnackbar("Positive button clicked")
        }
    }

    fun onNegativeButton() {
        coroutineScope.launch {
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
                    coroutineScope.launch { dismissCallback() }
                        .invokeOnCompletion { onPositiveButton() }
                },
                onNegativeButton = {
                    coroutineScope.launch { dismissCallback() }
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
        showDialog = showCustomAnimationDialog_BottomSheet,
        onDismissRequest = { showCustomAnimationDialog_BottomSheet = false },
    ) {
        DialogContent(
            onPositiveButton = {
                showCustomAnimationDialog_BottomSheet = false
                onPositiveButton()
            },
            onNegativeButton = {
                showCustomAnimationDialog_BottomSheet = false
                onNegativeButton()
            }
        )
    }
}

@Composable
fun DialogContent(
    onPositiveButton: () -> Unit,
    onNegativeButton: () -> Unit
) {
    Column {
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