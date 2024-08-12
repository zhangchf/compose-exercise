package com.zcf.composeexercise.photoharvesting

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode.Reverse
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest.Builder
import com.zcf.composeexercise.R
import com.zcf.composeexercise.photoharvesting.ImageCaptureType.UPC
import com.zcf.composeexercise.ui.theme.Colors
import com.zcf.composeexercise.ui.theme.TextStyles
import com.zcf.composeexercise.widget.clickableWithRoundRipple

@Composable
internal fun CameraOverlay(
  itemType: ItemType,
  imageCaptureType: ImageCaptureType,
  height: Dp,
  bottomImmersedHeight: Dp = CAMERA_VIEW_BOTTOM_IMMERSED_HEIGHT,
  isTorchOn: Boolean,
  isParsingBarcode: Boolean,
  onTakePicture: (ImageCaptureType) -> Unit,
  onSkip: () -> Unit,
  onSeeExamples: () -> Unit,
  onSwitchTorch: () -> Unit,
  onClose: () -> Unit
) {
  val overlayBackgroundColor = if (itemType == ItemType.SHOES && imageCaptureType != UPC) {
    Colors.black40
  } else {
    Color.Unspecified
  }
  val topGradientBrushBg = if (imageCaptureType != UPC) {
    Brush.verticalGradient(
      0f to Colors.background,
      1f to Color.Transparent
    )
  } else {
    SolidColor(Color.Unspecified)
  }

  // For taking picture animation
  var bottomContainerHeight by remember { mutableStateOf(0.dp) }
  var isTakingPicture by remember { mutableStateOf(false) }
  val animatedFlashAlpha = remember { Animatable(0f) }
  LaunchedEffect(key1 = isTakingPicture) {
    if (isTakingPicture) {
      animatedFlashAlpha.snapTo(1f)
      animatedFlashAlpha.animateTo(0f, tween(300, delayMillis = 100))
      isTakingPicture = false
    }
  }

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(height)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .height(height)
    ) {
      // Top container with Close and Flash buttons and camera instructions.
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .weight(5f)
          .background(overlayBackgroundColor)
          .background(topGradientBrushBg)
          .padding(top = 4.dp)
      ) {
        Row(modifier = Modifier.padding(horizontal = 4.dp)) {
          IconButton(onClick = { onClose() }) {
            Image(
              painter = painterResource(id = R.drawable.ic_close_24dp),
              contentDescription = "",
              colorFilter = ColorFilter.tint(Colors.gray3Solid)
            )
          }
          Spacer(modifier = Modifier.weight(1f))
          IconButton(onClick = { onSwitchTorch() }) {
            val torchRes = if (isTorchOn) R.drawable.camera_flash_on else R.drawable.camera_flash_off
            Image(
              painter = painterResource(id = torchRes),
              contentDescription = "",
              modifier = Modifier.size(24.dp)
            )
          }
        }
        Row(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Camera Tips",
            modifier = Modifier.weight(7f),
            style = TextStyles.bodyRegular(Colors.white)
          )
          Spacer(modifier = Modifier.width(20.dp))
          Box(
            modifier = Modifier.weight(3f),
            contentAlignment = Alignment.BottomEnd
          ) {
            if (imageCaptureType != UPC) {
              Text(
                text = "See Examples",
                textDecoration = TextDecoration.Underline,
                style = TextStyles.bodyRegular(Colors.white),
                modifier = Modifier.clickable { onSeeExamples() }
              )
            }
          }
        }
      }

      // Middle container with camera frame and overlay
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(1f),
        contentAlignment = Alignment.Center
      ) {
//        if (imageCaptureType == UPC) {
//          Box() {
//            Image(
//              painter = painterResource(id = R.drawable.camera_frame_lines_barcode),
//              contentDescription = ""
//            )
//            ScannerLine(modifier = Modifier.matchParentSize(), isScanning = isParsingBarcode)
//          }
//        } else {
//          if (itemType == ItemType.SHOES) {
//            Image(
//              modifier = Modifier.fillMaxSize(),
//              painter = painterResource(id = imageCaptureType.imageOverlay),
//              contentDescription = ""
//            )
//          } else {
//            Image(
//              painter = painterResource(id = R.drawable.ic_camera_cross),
//              contentDescription = ""
//            )
//          }
//          Image(
//            modifier = Modifier.fillMaxSize(),
//            painter = painterResource(id = R.drawable.camera_frame_lines),
//            contentDescription = ""
//          )
//        }
      }

      // Bottom container with skip button
      BoxWithConstraints(
        modifier = Modifier
          .fillMaxWidth()
          .weight(5f)
          .background(overlayBackgroundColor)
          .padding(horizontal = 20.dp)
          .padding(bottom = bottomImmersedHeight)
      ) {
        bottomContainerHeight = this.maxHeight
        Text(
          text = "Skip",
          textDecoration = TextDecoration.Underline,
          style = TextStyles.bodyRegular(Colors.white),
          modifier = Modifier
            .align(Alignment.CenterEnd)
            .clickable { onSkip() }
        )
      }
    }

    // Take picture animation overlay
    Box(
      modifier = Modifier
        .fillMaxSize()
        .alpha(animatedFlashAlpha.value)
        .background(Colors.windowBackground)
    )

    // Take picture button, put at the center of bottomContainer
    if (imageCaptureType != UPC) {
      val takePictureButtonSize = 66.dp
      val paddingBottom = if (bottomContainerHeight > takePictureButtonSize) {
        (bottomContainerHeight - takePictureButtonSize) / 2
      } else {
        0.dp
      }
      AsyncImage(
        model = Builder(LocalContext.current).data(R.drawable.ic_take_picture).build(),
        contentDescription = "",
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(bottom = paddingBottom + bottomImmersedHeight)
          .size(takePictureButtonSize)
          .clickableWithRoundRipple {
            isTakingPicture = true
            onTakePicture(imageCaptureType)
          }
      )
    }
  }
}

@Composable
internal fun ScannerLine(
  modifier: Modifier,
  isScanning: Boolean
) {
  BoxWithConstraints(modifier = modifier) {
    val width = this.maxWidth
    val height = this.maxHeight
    val widthPx = with(LocalDensity.current) { width.toPx() }
    val heightPx = with(LocalDensity.current) { height.toPx() }
    val strokeWith = with(LocalDensity.current) { 2.dp.toPx() }

    val animatedYPos = remember { Animatable(0f) }
    LaunchedEffect(key1 = isScanning) {
      if (isScanning) {
        animatedYPos.animateTo(
          targetValue = heightPx,
          animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = Reverse
          )
        )
      } else {
        animatedYPos.snapTo(0f)
      }
    }
    if (isScanning) {
      val yPos = animatedYPos.value
      Canvas(modifier = modifier) {
        drawLine(
          color = Colors.primary,
          start = Offset(0f, yPos),
          end = Offset(widthPx, yPos),
          strokeWidth = strokeWith
        )
      }
    }
  }
}