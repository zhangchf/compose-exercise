package com.zcf.composeexercise.pages

import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.zcf.composeexercise.R
import com.zcf.composeexercise.widget.BulletSpanText
import com.zcf.composeexercise.widget.setTextWithBulletSpan

@Composable
fun Texts() {
  Column {
    AndroidView(
      factory = { context ->
        TextView(context).apply {
          setTextWithBulletSpan(R.string.damagedBoxBody)
        }
      },
      modifier = Modifier.padding(20.dp)
    )

    BulletSpanText(
      textRes = R.string.damagedBoxBody1,
      modifier = Modifier.padding(20.dp)
    )
  }
}
