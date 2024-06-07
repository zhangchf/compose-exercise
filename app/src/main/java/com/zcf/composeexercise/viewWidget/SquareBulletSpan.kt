package com.onesell.utils.view.core

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.text.Layout
import android.text.Spanned
import android.text.style.LeadingMarginSpan
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.zcf.composeexercise.widget.dp2px

class SquareBulletSpan(
  resources: Resources,
  @Px val bulletSize: Int = resources.dp2px(DEFAULT_BULLET_SIZE_DP).toInt(),
  @Px val bulletGapWidth: Int = resources.dp2px(DEFAULT_BULLET_GAP_WIDTH_DP).toInt(),
  @Px val extraLeadingMargin: Int = bulletGapWidth,
  @ColorInt val color: Int? = null
) : LeadingMarginSpan {

  override fun getLeadingMargin(first: Boolean): Int = extraLeadingMargin + bulletSize + bulletGapWidth

  override fun drawLeadingMargin(
    canvas: Canvas,
    paint: Paint,
    x: Int,
    dir: Int,
    top: Int,
    baseline: Int,
    bottom: Int,
    text: CharSequence,
    start: Int,
    end: Int,
    first: Boolean,
    layout: Layout?
  ) {
    if ((text as Spanned).getSpanStart(this) == start) {
      val oldStyle = paint.style
      var oldColor = 0

      if (color != null) {
        oldColor = paint.color
        paint.color = color
      }
      paint.style = Paint.Style.FILL

      val lineBottom = if (layout != null) {
        // "bottom" position might include extra space as a result of line spacing
        // configuration. Subtract extra space in order to show bullet in the vertical
        // center of characters.
        if (VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {
          val line = layout.getLineForOffset(start)
          layout.getLineBottom(line, false)
        } else {
          val lineExtra = (layout.spacingAdd * layout.spacingMultiplier).toInt()
          bottom - lineExtra
        }
      } else {
        bottom
      }

      val bulletTop = (top + lineBottom - bulletSize) / 2f
      val bulletStart: Float = (x + extraLeadingMargin).toFloat()

      canvas.drawRect(bulletStart, bulletTop, bulletStart + bulletSize, bulletTop + bulletSize, paint)

      if (color != null) {
        paint.color = oldColor
      }
      paint.style = oldStyle
    }
  }

  companion object {
    const val DEFAULT_BULLET_GAP_WIDTH_DP = 10
    const val DEFAULT_BULLET_SIZE_DP = 5
  }
}
