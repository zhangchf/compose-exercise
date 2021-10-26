package com.zcf.composeexercise.pages

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import kotlinx.coroutines.*
import com.zcf.composeexercise.R

@Composable
fun canvasPage(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar {
                Text(text = "Canvas Demo")
            }
        },
        bottomBar = {}
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InstagramIcon { }
            FacebookIcon()

        }
    }
}

suspend fun backgroundWork() {
    coroutineScope {
        val result = async {
            delay(10000)
            "terere"
        }

        launch {  }
        println("result: ${result.await()}")
    }
}

@Composable
fun InstagramIcon(onClick: () -> Unit) {
    val instagramColor = listOf(Color.Yellow, Color.Red, Color.Magenta)
    val brush = Brush.linearGradient(instagramColor)
    Canvas(
        modifier = Modifier
            .size(100.dp)
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        drawRoundRect(
            brush = brush,
            cornerRadius = CornerRadius(60f, 60f),
            style = Stroke(width = 15f, cap = StrokeCap.Round)
        )
        drawCircle(
            brush = brush,
            radius = 45f,
            style = Stroke(width = 15f, cap = StrokeCap.Round)
        )
        drawCircle(
            brush = brush,
            radius = 13f,
            center = Offset(x = this.size.width * 0.8f, y = this.size.height * 0.2f)
        )
    }
}

@Composable
fun FacebookIcon() {
    val textPaint = Paint().apply {
        textSize = 200f
        color = Color.White.toArgb()
        typeface = ResourcesCompat.getFont(LocalContext.current, R.font.lato_regular)
    }
    Canvas(
        modifier = Modifier
            .size(100.dp)
            .padding(16.dp)
    ) {
        drawRoundRect(
            color = Color(0xff1776d1),
            cornerRadius = CornerRadius(20f)
        )
        drawContext.canvas.nativeCanvas.drawText("f", size.width * 0.5f, size.height * 0.9f, textPaint)
    }
}