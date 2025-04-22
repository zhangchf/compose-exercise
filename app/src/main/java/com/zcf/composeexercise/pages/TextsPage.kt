package com.zcf.composeexercise.pages

import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.zcf.composeexercise.R
import com.zcf.composeexercise.utils.toPx
import com.zcf.composeexercise.widget.BulletSpanText
import com.zcf.composeexercise.widget.setTextWithBulletSpan

@Composable
fun Texts() {
  val density = LocalDensity.current
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

    Box(
      modifier = Modifier.padding(20.dp)
    ) {
      val text = "A text with border    i     .     ,  9 ITEMS"
      val fontSize = 20.sp
      val style = TextStyle(fontSize = fontSize)
      Text(
        text = text,
        color = Color.Black,
        style = style.copy(
          drawStyle = Stroke(
            width = fontSize.toPx(density)*2/3,
            join = StrokeJoin.Round
          )
        )
      )
      Text(
        text = text,
        color = Color.Red,
        style = style
      )

    }
  }
}
