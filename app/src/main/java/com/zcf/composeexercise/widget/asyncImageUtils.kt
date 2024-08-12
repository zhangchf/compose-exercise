package org.alias.utils.view.compose

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest
import coil.request.ImageRequest.Builder

@Composable
fun asyncImageData(@DrawableRes drawableResId: Int): ImageRequest {
  return Builder(LocalContext.current).data(drawableResId).build()
}
