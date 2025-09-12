package com.zcf.composeexercise.utils

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

fun Dp.toPx(density: Density) = with(density) { toPx() }

fun TextUnit.toPx(density: Density) = with(density) { toPx() }

fun Int.toDp(density: Density): Dp = with(density) { toDp() }

fun Float.toDp(density: Density): Dp = with(density) { toDp() }
