package com.zcf.composeexercise.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.zcf.composeexercise.ui.theme.Colors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val ANIMATION_DURATION = 300
private const val DIALOG_BUILD_TIME = 0L

/**
 * Reference: https://medium.com/bilue/expanding-dialog-in-jetpack-compose-a6be40deab86
 */
@Composable
fun AnimatedTransitionDialog(
    onDismissRequest: () -> Unit,
    enter: EnterTransition = fadeIn() + expandIn(),
    exit: ExitTransition = shrinkOut() + fadeOut(),
    animationDuration: Int = ANIMATION_DURATION,
    content: @Composable (AnimatedTransitionDialogHelper) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var animatedVisible by remember { mutableStateOf(false) }
    val onDismissFlow = remember { MutableSharedFlow<Any>() }

    fun startDismissWithExitAnimation() = coroutineScope.launch {
        animatedVisible = false
        delay(animationDuration.toLong())
    }.invokeOnCompletion {
        onDismissRequest()
    }

    LaunchedEffect(Unit) {
        launch {
            delay(DIALOG_BUILD_TIME)
            animatedVisible = true
        }

        launch {
            onDismissFlow.collectLatest {
                startDismissWithExitAnimation()
            }
        }
    }

    Dialog(
        onDismissRequest = {
            startDismissWithExitAnimation()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visible = animatedVisible,
                enter = enter,
                exit = exit
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(Colors.windowBackground),
                    contentAlignment = Alignment.Center
                ) {
                    CompositionLocalProvider(LocalContentColor provides Colors.gray3Solid) {
                        content(AnimatedTransitionDialogHelper(coroutineScope, onDismissFlow))
                    }
                }
            }
        }
    }
}

class AnimatedTransitionDialogHelper(
    private val coroutineScope: CoroutineScope,
    private val onDismissFlow: MutableSharedFlow<Any>
) {
    fun triggerAnimatedDismiss() {
        coroutineScope.launch {
            onDismissFlow.emit(Any())
        }
    }
}