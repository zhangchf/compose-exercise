package com.zcf.composeexercise.pages

import androidx.compose.runtime.Composable
import com.zcf.composeexercise.photoharvesting.ImageCaptureType
import com.zcf.composeexercise.photoharvesting.PhotoHarvestingHostView

@Composable
fun PhotoHarvestingPage() {
  PhotoHarvestingHostView(
    onPictureCaptured = { imageCaptureType: ImageCaptureType, bytes: ByteArray ->
    },
    onBarcodeCaptured = {},
    onSeeExamples = {},
    onClose = {}
  )
}
