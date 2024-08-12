package org.alias.utils.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Size
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.zcf.composeexercise.utils.cropRectOfInterest
import com.zcf.composeexercise.utils.rotate

internal const val IMAGE_CAPTURE_MAX_WIDTH = 1000

@SuppressLint("RestrictedApi")
class CameraWrapper(
  private val context: Context,
  private val lifecycleOwner: LifecycleOwner
) {
  // Camera related
  val previewView by lazy { PreviewView(context) }
  private val preview by lazy { Preview.Builder().build() }
  private val imageCapture by lazy {
    val maxWidth = IMAGE_CAPTURE_MAX_WIDTH
    ImageCapture.Builder().setMaxResolution(Size(maxWidth, maxWidth)).build()
  }
  private val imageAnalysis by lazy { ImageAnalysis.Builder().build() }
  private var cameraProvider: ProcessCameraProvider? = null
  private var camera: Camera? = null

  fun init() {
    if (cameraProvider != null) return

    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    cameraProviderFuture.addListener({
      cameraProvider = cameraProviderFuture.get()
      val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
      try {
        cameraProvider?.unbindAll()
        camera = cameraProvider?.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture, imageAnalysis)
        preview.setSurfaceProvider(previewView.surfaceProvider)
      } catch (e: Exception) {
        println("Camera init failed")
      }
    }, ContextCompat.getMainExecutor(context))
  }

  fun stop() {
    if (cameraProvider == null) return
    cameraProvider?.unbindAll()
    cameraProvider = null
  }

  fun enableTorch(on: Boolean) {
    camera?.cameraControl?.enableTorch(on)
  }

  fun enableFlash(on: Boolean) {
    imageCapture.flashMode = if (on) ImageCapture.FLASH_MODE_ON else ImageCapture.FLASH_MODE_OFF
  }

  fun enablePreview(on: Boolean) {
    preview.setSurfaceProvider(
      if (on) previewView.surfaceProvider else null
    )
  }

  fun scanBarcode(onResult: (barcode: String) -> Unit) {
    imageAnalysis.parseBarcode(context) { barcode ->
      onResult(barcode)
    }
  }

  fun takePicture(onResult: (picture: Bitmap) -> Unit) {
    imageCapture.takePictureAndCrop(context) { picture ->
      onResult(picture)
    }
  }
}

internal fun ImageCapture.takePictureAndCrop(
  context: Context,
  onPictureCaptured: (picture: Bitmap) -> Unit
) {
  this.takePicture(
    ContextCompat.getMainExecutor(context),
    object : OnImageCapturedCallback() {
      override fun onCaptureSuccess(image: ImageProxy) {
        super.onCaptureSuccess(image)

        val capturedPicture = image.toBitmap().rotate(image.imageInfo.rotationDegrees.toFloat())
        onPictureCaptured(capturedPicture)

        image.close()
      }

      override fun onError(exception: ImageCaptureException) {
        super.onError(exception)
      }
    }
  )
}

internal fun ImageAnalysis.parseBarcode(
  context: Context,
  onBarcodeParsed: (barcode: String) -> Unit
) {
  val options = BarcodeScannerOptions.Builder()
    .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
    .build()
  val barcodeScanner = BarcodeScanning.getClient(options)

  this.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
    val bitmap = imageProxy.toBitmap()
      .rotate(imageProxy.imageInfo.rotationDegrees.toFloat())
      .cropRectOfInterest(0f, 0.3f, 1f, 0.7f)

    barcodeScanner.process(InputImage.fromBitmap(bitmap, 0))
      .addOnSuccessListener { barcodeList ->
        barcodeList.firstOrNull()?.rawValue?.let {
          this.clearAnalyzer()
          onBarcodeParsed(it)
        }
      }
      .addOnCompleteListener {
        imageProxy.close()
      }
  }
}
