package com.zcf.composeexercise.widget

import android.graphics.Paint
import android.text.Annotation
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.SpannedString
import android.text.method.LinkMovementMethod
import android.text.style.BulletSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.LeadingMarginSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible

inline var TextView.isUnderlined: Boolean
  get() = paintFlags and Paint.UNDERLINE_TEXT_FLAG > 0
  set(value) {
    paintFlags = if (value) {
      paintFlags or Paint.UNDERLINE_TEXT_FLAG
    } else {
      paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
    }
  }

fun TextView.setTextOrHideIfNullOrEmpty(text: CharSequence?) {
  if (text.isNullOrEmpty()) {
    isVisible = false
  } else {
    setText(text)
  }
}

fun TextView.setGlowColor(@ColorInt color: Int) {
  setTextColor(color)
  setShadowLayer(shadowRadius, shadowDx, shadowDy, color)
}

inline var TextView.isStrikeThru: Boolean
  get() = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG > 0
  set(value) {
    paintFlags = if (value) {
      paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
      paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
  }

/**
 * Set text with 1 clickable span.
 * The span should be enclosed in <annotation> tag: <annotation tag="clickable">
 */
fun TextView.setTextWithClickableSpan(
  @StringRes textRes: Int,
  onClickSpan: () -> Unit,
  @ColorRes spanColor: Int? = null
) = this.setTextWithClickableSpan(
  textRes = textRes,
  onClickSpan1 = onClickSpan,
  onClickSpan2 = null,
  spanColor = spanColor
)

/**
 * Set text with 1-2 clickable spans.
 * The spans should be enclosed in <annotation> tags:
 * First one should be <annotation tag="clickable1">,
 * and second one should be <annotation tag="clickable2">
 */
fun TextView.setTextWithClickableSpan(
  @StringRes textRes: Int,
  onClickSpan1: () -> Unit,
  onClickSpan2: (() -> Unit)? = null,
  @ColorRes spanColor: Int? = null
) {
  val fullText = resources.getText(textRes)
  val spansInfo = (fullText as? SpannedString)?.getSpansInfo()

  val fgColorSpan = spanColor?.let {
    ForegroundColorSpan(resources.getColor(spanColor, null))
  }
  text = SpannableStringBuilder(fullText).apply {
    spansInfo?.clickableSpan1?.let { (spanStart, spanEnd) ->
      val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
          onClickSpan1.invoke()
        }
      }
      setSpan(clickableSpan, spanStart, spanEnd, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
      fgColorSpan?.let {
        setSpan(fgColorSpan, spanStart, spanEnd, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
      }
    }

    spansInfo?.clickableSpan2?.let { (spanStart, spanEnd) ->
      val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
          onClickSpan2?.invoke()
        }
      }
      setSpan(clickableSpan, spanStart, spanEnd, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
      fgColorSpan?.let {
        setSpan(fgColorSpan, spanStart, spanEnd, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
      }
    }
  }
  movementMethod = LinkMovementMethod.getInstance()
}

/**
 * Set text with bullet spans.
 * The spans should be enclosed in <annotation> tags: <annotation tag="bullet">xxx</annotation>
 */
fun TextView.setTextWithBulletSpan(
  @StringRes textRes: Int,
  bulletGapWidthDp: Int = DEFAULT_BULLET_GAP_WIDTH_DP,
  extraLeadingMarginDp: Int = bulletGapWidthDp
) {
  val fullText = resources.getText(textRes)
  val spansInfo = (fullText as? SpannedString)?.getSpansInfo()

  val bulletGapWidth = dp2px(bulletGapWidthDp)
  val extraLeadingMargin = dp2px(extraLeadingMarginDp)

  text = SpannableStringBuilder(fullText).apply {
    spansInfo?.bulletSpans?.forEach { (spanStart, spanEnd) ->
      setSpan(LeadingMarginSpan.Standard(extraLeadingMargin), spanStart, spanEnd, 0)
      setSpan(BulletSpan(bulletGapWidth), spanStart, spanEnd, 0)
    }
  }
}

fun SpannedString.getSpansInfo(): SpansInfo {
  val spansInfo = SpansInfo()
  val bulletSpans: MutableList<Pair<Int, Int>> = mutableListOf()
  val spans = getSpans(0, length, Annotation::class.java)
  spans?.forEach { span ->
    if (span.key == "tag") {
      when (span.value) {
        TAG_CLICKABLE_SPAN, TAG_CLICKABLE_SPAN1 -> {
          spansInfo.clickableSpan1 = Pair(getSpanStart(span), getSpanEnd(span))
        }
        TAG_CLICKABLE_SPAN2 -> {
          spansInfo.clickableSpan2 = Pair(getSpanStart(span), getSpanEnd(span))
        }
        TAG_BULLET_SPAN -> {
          bulletSpans.add(Pair(getSpanStart(span), getSpanEnd(span)))
        }
      }
    }
  }
  spansInfo.bulletSpans = bulletSpans
  return spansInfo
}

data class SpansInfo(
  var clickableSpan1: Pair<Int, Int>? = null,
  var clickableSpan2: Pair<Int, Int>? = null,
  var bulletSpans: List<Pair<Int, Int>>? = null
)

const val TAG_CLICKABLE_SPAN = "clickable"
const val TAG_CLICKABLE_SPAN1 = "clickable1"
const val TAG_CLICKABLE_SPAN2 = "clickable2"
const val TAG_BULLET_SPAN = "bullet"
const val DEFAULT_BULLET_GAP_WIDTH_DP = 10
