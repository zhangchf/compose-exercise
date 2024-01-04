package com.zcf.composeexercise.widget

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.graphics.Color
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
import com.zcf.composeexercise.widget.BottomSheetState.Collapsed
import com.zcf.composeexercise.widget.BottomSheetState.Expanded

const val DIALOG_ANIM_DURATION = 300
/**
 * This is the same as AliasBottomSheetDialog
 *
 * Reference 1: https://www.sinasamaki.com/custom-dialog-animation-in-jetpack-compose/
 *      gist: https://gist.github.com/sinasamaki/daa825d96235a18822177a2b1b323f49?ref=sinasamaki.com
 *
 * Reference 2: https://proandroiddev.com/how-to-master-swipeable-and-nestedscroll-modifiers-in-compose-bb0635d6a760
 *      gist: https://gist.github.com/arcadefire/7fe138c0ded1a36bee6dd57acdfa3d18
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomAnimationDialog_Draggable(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    isCancellable: Boolean = true,
    enter: EnterTransition =
        slideInVertically(tween(DIALOG_ANIM_DURATION)) { it } + fadeIn(tween(DIALOG_ANIM_DURATION)),
    exit: ExitTransition =
        slideOutVertically(tween(DIALOG_ANIM_DURATION)) { it } + fadeOut(tween(DIALOG_ANIM_DURATION)),
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
            val swipeableState = rememberSwipeableState(initialValue = Expanded) { state ->
                if (state == Collapsed) {
                    onDismissRequest()
                }
                true
            }
            //  val lazyListState = rememberLazyListState()
            val innerScrollState = rememberScrollState()

            val nestedConnection = remember {
                BottomSheetNestedScrollConnection(
                    swipeableState = swipeableState,
                    innerScrollableReachedTop = {
//                        lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0
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
                        .background(Color.Black.copy(alpha = 0.56f))
                        .fillMaxSize()
                        .then(
                            if (isCancellable) {
                                Modifier
                                    .pointerInput(Unit) {
                                        detectTapGestures { onDismissRequest() }
                                    }
                                    .pointerInput(Unit) {
                                        detectDragGestures { change, dragAmount -> }
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
                                                0f to Expanded,
                                                totalHeight + 100f to Collapsed
                                            ),
                                            orientation = Orientation.Vertical,
                                            thresholds = {_, _ -> FractionalThreshold(0.7f)}
                                        )
                                        .nestedScroll(nestedConnection)
                                } else {
                                    Modifier
                                }
                            )
                            .fillMaxWidth()
//                            .heightIn(max = 500.dp) // for testing
                            .clip(roundedShape)
                            .background(MaterialTheme.colors.surface),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(innerScrollState)
                        ) {
                            Column {
                                content()
                                content()
                                content()
                            }
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

@OptIn(ExperimentalMaterialApi::class)
private class BottomSheetNestedScrollConnection(
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

private enum class BottomSheetState {
    Expanded, Collapsed
}
