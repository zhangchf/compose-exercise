package com.zcf.composeexercise.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun GesturesPage() {
    Scaffold(
        topBar = {
            TopAppBar {
                Text(text = "Gestures")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            DraggableInOneDirection()
            DraggableFreely()

            ScrollableSample()
        }

    }
}

@Composable
fun DraggableInOneDirection() {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    Text(
        "Draggable in 1 direction",
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .background(Color.Green)
            .draggable(
                state = DraggableState { delta ->
                    offsetX += delta
                },
                orientation = Orientation.Horizontal
            )
            .draggable(
                state = DraggableState { deltaY ->
                    offsetY += deltaY
                },
                orientation = Orientation.Vertical
            )
    )
}

@Composable
fun DraggableFreely() {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    Text(
        text = "Drag freely",
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .background(Color.Magenta)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consumeAllChanges()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
    )
}

@Composable
private fun ScrollableSample() {
    // actual composable state
    var offset by remember { mutableStateOf(0f) }

    val scrollState = rememberScrollState()
    scrollState.interactionSource.collectIsDraggedAsState()
    Box(
        Modifier
            .size(150.dp)
            .horizontalScroll(scrollState)
            .scrollable(
                orientation = Orientation.Vertical,
                // Scrollable state: describes how to consume
                // scrolling delta and update offset
                state = rememberScrollableState { delta ->
                    offset += delta
                    delta
                }
            )
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text(offset.toString())
    }
}