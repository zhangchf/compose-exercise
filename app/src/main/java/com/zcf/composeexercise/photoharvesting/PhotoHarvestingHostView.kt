package com.zcf.composeexercise.photoharvesting

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.LongPress
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.zcf.composeexercise.photoharvesting.ImageCaptureType.BACK
import com.zcf.composeexercise.photoharvesting.ImageCaptureType.INNER
import com.zcf.composeexercise.photoharvesting.ImageCaptureType.OUTER
import com.zcf.composeexercise.photoharvesting.ImageCaptureType.TOP
import com.zcf.composeexercise.photoharvesting.ImageCaptureType.UPC
import com.zcf.composeexercise.ui.theme.Colors
import com.zcf.composeexercise.utils.compress
import com.zcf.composeexercise.utils.cropCenterRect
import com.zcf.composeexercise.widget.ComposeSingleButtonBottomSheetDialog
import com.zcf.composeexercise.widget.openAppSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.alias.utils.camera.CameraWrapper

internal val BOTTOM_SHEET_PEEK_HEIGHT = 110.dp
internal val BOTTOM_SHEET_TOP_MARGIN = 40.dp
internal val CAMERA_VIEW_BOTTOM_IMMERSED_HEIGHT = 20.dp

enum class ItemType {
  SHOES,
  APPAREL
}

enum class ImageCaptureType {
  OUTER,
  INNER,
  TOP,
  BACK,
  UPC
}

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun PhotoHarvestingHostView(
  itemType: ItemType = ItemType.SHOES,
  requiredPictures: List<ImageCaptureType> = listOf(OUTER, INNER, TOP, BACK, UPC),
  startIndex: Int = 0,
  onPictureCaptured: (ImageCaptureType, ByteArray) -> Unit,
  onBarcodeCaptured: (String) -> Unit,
  onSeeExamples: () -> Unit,
  onClose: () -> Unit
) {
  var screenHeight by remember { mutableStateOf(0.dp) }
  val bottomSheetPeekHeight = BOTTOM_SHEET_PEEK_HEIGHT

  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current
  val hapticFeedback = LocalHapticFeedback.current
  val coroutineScope = rememberCoroutineScope()

  // Permission related
  var hasCameraPermission by remember { mutableStateOf(false) }
  var showPermissionRationaleDialog by remember { mutableStateOf(false) }

  // Camera related
  val cameraWrapper = remember { CameraWrapper(context, lifecycleOwner) }

  // Camera view states
  var currentPictureIndex by remember { mutableIntStateOf(startIndex) }
  val capturedPictures = remember { mutableStateMapOf<Int, Bitmap?>() }
  var capturedBarcode by remember { mutableStateOf<String?>(null) }
  var isParsingBarcode by remember { mutableStateOf(false) }
  var isTorchOn by remember { mutableStateOf(false) }
  var previewWidthPx by remember { mutableFloatStateOf(0f) }
  var previewHeightPx by remember { mutableFloatStateOf(0f) }

  val requestPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { hasPermission ->
    if (hasPermission) {
      hasCameraPermission = true
    } else {
      showPermissionRationaleDialog = true
    }
  }

  // Check camera permission
  LifecycleResumeEffect(Unit) {
    if (context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
      hasCameraPermission = true
    } else {
      hasCameraPermission = false
      requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
    onPauseOrDispose { }
  }

  // Init camera
  DisposableEffect(hasCameraPermission) {
    if (hasCameraPermission) {
      cameraWrapper.init()
    }
    onDispose {
      cameraWrapper.stop()
    }
  }

  fun isOnUpcPage() = requiredPictures.getOrNull(currentPictureIndex) == UPC

  fun delayedClose() {
    coroutineScope.launch {
      delay(1000)
      onClose()
    }
  }

  fun enableTorch(on: Boolean) {
    cameraWrapper.enableFlash(on)
    if (isOnUpcPage()) {
      cameraWrapper.enableTorch(on)
    }
  }

  fun scanBarcode() {
    isParsingBarcode = true
    cameraWrapper.scanBarcode { barcode ->
      hapticFeedback.performHapticFeedback(LongPress)
      capturedBarcode = barcode
      onBarcodeCaptured(barcode)
      isParsingBarcode = false
      cameraWrapper.enablePreview(false)
      delayedClose()
    }
  }

  fun checkAndRestartProcess() {
    cameraWrapper.enablePreview(true)
    cameraWrapper.enableTorch(isTorchOn && isOnUpcPage())
    if (isOnUpcPage()) {
      scanBarcode()
    }
  }

  fun movePictureIndex() {
    if (currentPictureIndex < requiredPictures.size - 1) {
      currentPictureIndex += 1
    } else {
      delayedClose()
    }
    checkAndRestartProcess()
  }

  fun takePicture(imageCaptureType: ImageCaptureType) {
    cameraWrapper.takePicture() { picture ->
      hapticFeedback.performHapticFeedback(LongPress)
      val croppedPicture = picture.cropCenterRect(previewWidthPx, previewHeightPx, previewWidthPx)
      capturedPictures[currentPictureIndex] = croppedPicture
      onPictureCaptured(imageCaptureType, croppedPicture.compress())
      movePictureIndex()
    }
  }

  val scaffoldState = rememberBottomSheetScaffoldState()
  BottomSheetScaffold(
    sheetContent = {
      BottomSheetRootColumn(
        bottomSheetHeight = screenHeight - BOTTOM_SHEET_TOP_MARGIN,
        bottomSheetBackgroundColor = Colors.windowBackground
      ) {
        if (requiredPictures.isNotEmpty()) {
          PhotoHarvestingBottomSheet(
            itemType = itemType,
            requiredPictures = requiredPictures,
            currentPictureIndex = currentPictureIndex,
            capturedPictures = capturedPictures,
            capturedBarcode = capturedBarcode,
            onCaptureDone = {
              onClose()
            },
            onRetakePicture = {
              coroutineScope.launch {
                scaffoldState.bottomSheetState.collapse()
              }
              checkAndRestartProcess()
            },
            onSelectPicture = { pictureIndex ->
              currentPictureIndex = pictureIndex
              checkAndRestartProcess()
            }
          )
        }
      }
    },
    scaffoldState = scaffoldState,
    sheetBackgroundColor = Color.Transparent,
    sheetPeekHeight = bottomSheetPeekHeight,
    backgroundColor = Colors.windowBackground
  ) {
    BoxWithConstraints {
      screenHeight = this.maxHeight
      val cameraViewHeight =
        screenHeight - BOTTOM_SHEET_PEEK_HEIGHT + CAMERA_VIEW_BOTTOM_IMMERSED_HEIGHT
      val screenWidth = this.maxWidth
      previewWidthPx = with(LocalDensity.current) { screenWidth.toPx() }
      previewHeightPx = with(LocalDensity.current) { cameraViewHeight.toPx() }

      if (hasCameraPermission) {
        AndroidView(
          modifier = Modifier
            .fillMaxWidth()
            .height(cameraViewHeight),
          factory = { cameraWrapper.previewView }
        )
      }

      if (requiredPictures.isNotEmpty()) {
        CameraOverlay(
          itemType = itemType,
          imageCaptureType = requiredPictures[currentPictureIndex],
          height = cameraViewHeight,
          isTorchOn = isTorchOn,
          isParsingBarcode = isParsingBarcode,
          onTakePicture = { imageCaptureType ->
            takePicture(imageCaptureType)
          },
          onSkip = { movePictureIndex() },
          onSeeExamples = onSeeExamples,
          onSwitchTorch = {
            isTorchOn = !isTorchOn
            enableTorch(isTorchOn)
          },
          onClose = onClose
        )
      }

      ComposeSingleButtonBottomSheetDialog(
        showDialog = showPermissionRationaleDialog,
        onDismissRequest = { showPermissionRationaleDialog = false },
        title = "Camera Access Required",
        message = "Allow Camera permission in settings",
        button = "Open App Settings"
      ) {
        showPermissionRationaleDialog = false
        context.openAppSettings()
      }
    }
  }
}
