package com.zcf.composeexercise.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@Composable
fun GesturesPage() {
    Scaffold(
        topBar = {
            TopAppBar {
                Text(text = "Gestures")
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            DraggableInOneDirection()
            DraggableFreely()

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