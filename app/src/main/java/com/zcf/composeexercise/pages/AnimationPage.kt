package com.zcf.composeexercise.pages

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.PI
import kotlin.math.sin

data class Star(
    var x: Float,
    var y: Float,
    var alpha: Float
) {
    private val initialAlpha = alpha

    fun update(value: Float) {
        val x = value - initialAlpha
        alpha = (0.5 + 0.5 * sin(x)).toFloat()
    }
}

@Composable
fun animationPage() {
    Space()
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun Space(
    modifier: Modifier = Modifier
) {

    val infiniteTransition = rememberInfiniteTransition()
    val infiniteAnimatedFloat = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            tween(10000),
            repeatMode = RepeatMode.Restart
        )
    )

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth().aspectRatio(1f)
    ) {
        val density = LocalDensity.current
        val width = with(density) { maxWidth.toPx() }
        val height = with(density) { maxHeight.toPx()}

        val stars = remember {
            buildList {
                repeat(1000) {
                    val x = Math.random() * width
                    val y = Math.random() * height
                    val alpha = Math.random() * 2 * Math.PI
                    add(Star(x.toFloat(), y.toFloat(), alpha.toFloat()))
                }
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            stars.forEach { star ->
                star.update(infiniteAnimatedFloat.value)
                drawCircle(
                    color = Color.White,
                    center = Offset(star.x, star.y),
                    radius = 2f,
                    alpha = star.alpha
                )
            }
        }
    }
}