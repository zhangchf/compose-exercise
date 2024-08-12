package com.zcf.composeexercise.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import java.io.ByteArrayOutputStream

fun Bitmap.rotate(degrees: Float): Bitmap {
  val matrix = Matrix().apply { postRotate(degrees) }
  return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.compress(quality: Int = 50): ByteArray {
  val outputStream = ByteArrayOutputStream()
  compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
  return outputStream.toByteArray()
}

/**
 * Crop a center square area from a preview.
 * The preview size might be different with the bitmap, and the image would have been scaled to fit into the preview.
 */
fun Bitmap.cropCenterRect(previewWidth: Float, previewHeight: Float, cropSize: Float): Bitmap {
  val aspectRatio = 1f * this.width / this.height
  val previewRatio = 1f * previewWidth / previewHeight

  val (scaledWidth, scaledHeight) = if (aspectRatio > previewRatio) {
    Pair(previewHeight * aspectRatio, previewHeight)
  } else {
    Pair(previewWidth, previewWidth / aspectRatio)
  }
  val scale = scaledWidth / this.width

  val startX = ((scaledWidth - cropSize) / 2 / scale).toInt()
  val startY = ((scaledHeight - cropSize) / 2 / scale).toInt()
  val newSize = (cropSize / scale).toInt()
  return Bitmap.createBitmap(this, startX, startY, newSize, newSize, null, true)
}

/**
 * Crop a rect area of interest.
 * @param left left edge in percentage of the bitmap width, 0f - 1f.
 * @param top top edge in percentage of bitmap height, 0f - 1f.
 * @param right right edge in percentage of the bitmap width, 0f - 1f.
 * @param bottom bottom edge in percentage of bitmap height, 0f - 1f.
 */
fun Bitmap.cropRectOfInterest(left: Float, top: Float, right: Float, bottom: Float): Bitmap {
  require(right > left && bottom > top)
  val startX = (this.width * left).toInt()
  val startY = (this.height * top).toInt()
  val width = (this.width * (right - left)).toInt()
  val height = (this.height * (bottom - top)).toInt()
  return Bitmap.createBitmap(this, startX, startY, width, height, null, true)
}
