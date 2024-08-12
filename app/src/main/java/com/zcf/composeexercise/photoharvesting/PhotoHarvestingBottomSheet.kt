package com.zcf.composeexercise.photoharvesting

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.zcf.composeexercise.R
import com.zcf.composeexercise.photoharvesting.ImageCaptureType.UPC
import com.zcf.composeexercise.ui.theme.Colors
import com.zcf.composeexercise.ui.theme.TextStyles
import org.alias.utils.view.compose.asyncImageData

@Composable
internal fun ColumnScope.PhotoHarvestingBottomSheet(
  itemType: ItemType,
  requiredPictures: List<ImageCaptureType>,
  currentPictureIndex: Int,
  capturedPictures: SnapshotStateMap<Int, Bitmap?>,
  capturedBarcode: String?,
  onCaptureDone: () -> Unit,
  onRetakePicture: (pictureIndex: Int) -> Unit,
  onSelectPicture: (pictureIndex: Int) -> Unit
) {
  Spacer(modifier = Modifier.height(16.dp))
  Text(
    text = "Image Caption".uppercase(),
    style = TextStyles.c1Medium(Colors.gray3Solid)
  )

  fun capturedAt(index: Int): Boolean {
    val imageCaptureType = requiredPictures[index]
    return if (imageCaptureType == UPC) {
      capturedBarcode != null
    } else {
      capturedPictures[index] != null
    }
  }

  Spacer(modifier = Modifier.height(12.dp))
  Row(verticalAlignment = Alignment.CenterVertically) {
    requiredPictures.forEachIndexed { index, imageCaptureType ->
      Box(modifier = Modifier.size(40.dp)) {
        val model = when (imageCaptureType) {
          UPC -> asyncImageData(drawableResId = R.drawable.ic_thumb_optional)
          else -> capturedPictures[index] ?: asyncImageData(drawableResId = R.drawable.ic_thumb_optional)
        }
        AsyncImage(
          model = model,
          contentDescription = imageCaptureType.name,
          modifier = Modifier
            .size(40.dp)
            .border(
              width = 1.dp,
              color = if (index == currentPictureIndex) Colors.gray3Solid else Color.Unspecified
            )
            .clickable { onSelectPicture(index) },
          alpha = if (index == currentPictureIndex || capturedAt(index)) 1f else 0.2f
        )
        if (imageCaptureType == UPC && capturedBarcode != null) {
          Image(
            painter = painterResource(id = R.drawable.ic_circled_check),
            contentDescription = "",
            modifier = Modifier
              .align(Alignment.BottomEnd)
              .padding(bottom = 9.dp, end = 6.dp)
          )
        }
      }
      Spacer(modifier = Modifier.width(4.dp))
    }

    Spacer(modifier = Modifier.weight(1f))
    val totalCount = requiredPictures.size
    if (currentPictureIndex == totalCount - 1) {
      Text(
        text = "Done".uppercase(),
        style = TextStyles.c1Medium(Colors.primary),
        modifier = Modifier.clickable { onCaptureDone() }
      )
    } else {
      Text(
        text = String.format("%s / %s", currentPictureIndex + 1, totalCount).uppercase(),
        style = TextStyles.c1Medium(Colors.gray2Solid)
      )
    }
  }

  Spacer(modifier = Modifier.height(36.dp))
  if (capturedAt(currentPictureIndex)) {
    val imageCaptureType = requiredPictures[currentPictureIndex]
    if (imageCaptureType == UPC) {
      Text(
        text = "Barcode captured",
        style = TextStyles.bodyRegular(Colors.gray2Solid)
      )
    } else {
      AsyncImage(
        model = capturedPictures[currentPictureIndex],
        contentDescription = "",
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(1f)
      )
    }
    Spacer(modifier = Modifier.height(24.dp))
    Text(
      text = "Retake".uppercase(),
      style = TextStyles.c1Medium(Colors.links),
      modifier = Modifier.clickable {
        onRetakePicture(currentPictureIndex)
      }
    )
  }
}

@Composable
internal fun BottomSheetRootColumn(
  bottomSheetHeight: Dp,
  bottomSheetBackgroundColor: Color,
  content: @Composable ColumnScope.() -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .height(bottomSheetHeight)
      .background(
        color = bottomSheetBackgroundColor,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
      )
      .padding(20.dp)
  ) {
    Spacer(modifier = Modifier.height(8.dp))
    BottomSheetDragHandle()

    content()
  }
}

@Composable
internal fun ColumnScope.BottomSheetDragHandle() {
  Box(
    modifier = Modifier
      .width(50.dp)
      .height(3.dp)
      .background(
        color = Colors.gray2Solid,
        shape = RoundedCornerShape(percent = 50)
      )
      .align(Alignment.CenterHorizontally)
  )
}