package com.zcf.composeexercise.widget

import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider

/**
 * Reference: https://www.sinasamaki.com/custom-dialog-animation-in-jetpack-compose/
 * gist: https://gist.github.com/sinasamaki/daa825d96235a18822177a2b1b323f49?ref=sinasamaki.com
 */
@Composable
fun CustomAnimationDialog_BottomSheet(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    enter: EnterTransition = slideInVertically { it },
    exit: ExitTransition = slideOutVertically { it },
    content: @Composable () -> Unit
) {
    var showAnimatedDialog by remember { mutableStateOf(false) }

    LaunchedEffect(showDialog) {
        if (showDialog) showAnimatedDialog = true
    }

    if (showAnimatedDialog) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(usePlatformDefaultWidth = false)
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
                        .pointerInput(Unit) { detectTapGestures { onDismissRequest() } }
                        .background(Color.Black.copy(alpha = 0.56f))
                        .fillMaxSize()
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
                    val roundedShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    Box(
                        modifier = Modifier
                            .pointerInput(Unit) { detectTapGestures { } }
                            .shadow(8.dp, shape = roundedShape)
                            .fillMaxWidth()
                            .clip(roundedShape)
                            .background(MaterialTheme.colors.surface),
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

/**
 * Reference: [findDialogWindowProviderInParent] method in [AndroidImageHelpers.android.kt]
 */
internal fun findDialogWindowProviderInParent(view: View): DialogWindowProvider? {
    if (view is DialogWindowProvider) {
        return view
    }
    val parent = view.parent ?: return null
    if (parent is View) {
        return findDialogWindowProviderInParent(parent)
    }
    return null
}