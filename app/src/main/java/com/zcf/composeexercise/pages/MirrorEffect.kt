package com.zcf.composeexercise.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.zcf.composeexercise.R


/**
 * Reference: https://proandroiddev.com/mirror-effect-with-jetpack-compose-78db11b5c30b
 */
@Composable
fun MirrorPage() {
    Mirror(
        modifier = Modifier.fillMaxHeight(0.7f)
    ) {
        Image(
            painter = painterResource(id = R.drawable.sky_sea_mountain),
            contentDescription = "Sky sea mountain",
            alignment = Alignment.BottomCenter,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun Mirror(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.border(2.dp, Color.Green)
    ) {
        Box(
            modifier = Modifier.weight(2f)
        ) {
            content()
        }
        Box(
            modifier = Modifier.weight(1f)
                .border(4.dp, Color.Red)
                .graphicsLayer {
                    alpha = 0.8f
                    rotationX = 180f
                }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(listOf(Color.Transparent, Color.White)),
                        blendMode = BlendMode.DstIn
                    )
                }
                .blur(
                    radiusX = 1.dp,
                    radiusY = 3.dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                )

        ) {
            content()
        }
    }
}

object HalfSizeShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            Rect(
                offset = Offset(0f, size.height / 2),
                size = Size(size.width, size.height)
            )
        )
    }
}