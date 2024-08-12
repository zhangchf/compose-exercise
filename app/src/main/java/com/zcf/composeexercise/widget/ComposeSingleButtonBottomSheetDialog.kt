package com.zcf.composeexercise.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zcf.composeexercise.ui.theme.Colors
import com.zcf.composeexercise.ui.theme.Dimens
import com.zcf.composeexercise.ui.theme.TextStyles

@Composable
fun ComposeSingleButtonBottomSheetDialog(
  showDialog: Boolean,
  onDismissRequest: () -> Unit,
  isCancellable: Boolean = true,
  title: String,
  message: String,
  button: String,
  onClickButton: () -> Unit
) {
  AliasBottomSheetDialog(
    showDialog = showDialog,
    onDismissRequest = onDismissRequest,
    isCancellable = isCancellable
  ) {
    Column(
      modifier = Modifier
        .padding(horizontal = Dimens.pageHorizontalPadding)
        .padding(top = 32.dp, bottom = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = title,
        style = TextStyles.h2Medium(Colors.gray3Solid)
      )
      Spacer(modifier = Modifier.height(16.dp))
      Text(
        text = message,
        textAlign = TextAlign.Center,
        style = TextStyles.bodyRegular(Colors.gray3Solid)
      )
      Spacer(modifier = Modifier.height(32.dp))
      AliasOutlinedButton(
        title = button,
        isOutlined = false
      ) {
        onClickButton()
      }
    }
  }
}
