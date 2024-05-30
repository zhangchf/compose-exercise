package com.zcf.composeexercise.widget

import android.text.SpannedString
import android.util.Log
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import com.zcf.composeexercise.ui.theme.Colors

const val TAG = "ClickableSpanText"
/**
 * A clickable text with 1-2 clickable spans. Span text must exist in original text.
 *
 * For spannable strings without placeholder parameters, prefer [ClickableSpanText] or [ClickableSpanText].
 */
@Composable
fun ClickableSpanText(
  modifier: Modifier = Modifier,
  fullText: String,
  span: String,
  onClickSpan: (() -> Unit),
  span2: String? = null,
  onClickSpan2: (() -> Unit)? = null,
  style: TextStyle = LocalTextStyle.current,
  @ColorRes spanColor: Color = Colors.links,
  ignoreCase: Boolean = false,
  hasUnderline: Boolean = true
) {
  val spanStart = fullText.indexOf(span, ignoreCase = ignoreCase)
  val spanEnd = spanStart + span.length
  var span2Start: Int = -1
  var span2End: Int = -1
  val spanStyle = SpanStyle(
    color = spanColor,
    textDecoration = if (hasUnderline) TextDecoration.Underline else null
  )
  val annotatedString = androidx.compose.ui.text.buildAnnotatedString {
    append(fullText)
    if (spanStart >= 0) {
      addStyle(style = spanStyle, start = spanStart, end = spanEnd)
    } else {
      Log.e(TAG, "Span doesn't match: $span not in $fullText")
    }
    span2?.let {
      span2Start = fullText.indexOf(span2, ignoreCase = ignoreCase)
      span2End = span2Start + span2.length
      if (span2Start >= 0) {
        addStyle(style = spanStyle, start = span2Start, end = span2End)
      } else {
        Log.e(TAG, "Span doesn't match: $span2 not in $fullText")
      }
    }
  }
  ClickableText(
    text = annotatedString,
    modifier = modifier,
    style = style,
    onClick = { offset ->
      if (spanStart >= 0 && offset in (spanStart..spanEnd)) {
        onClickSpan()
      }
      if (span2 != null && span2Start >= 0 && offset in (span2Start..span2End)) {
        onClickSpan2?.invoke()
      }
    }
  )
}

/**
 * A clickable text with 1 clickable span.
 * The span should be enclosed in <annotation> tag: <annotation tag="clickable">
 *
 * This is preferred approach for strings without placeholder parameters.
 */
@Composable
fun ClickableSpanText(
  modifier: Modifier = Modifier,
  @StringRes textRes: Int,
  onClickSpan: (() -> Unit),
  style: TextStyle = LocalTextStyle.current,
  @ColorRes spanColor: Color = Colors.links,
  hasUnderline: Boolean = true
) = ClickableSpanText(
  modifier = modifier,
  textRes = textRes,
  onClickSpan1 = onClickSpan,
  onClickSpan2 = null,
  style = style,
  spanColor = spanColor,
  hasUnderline = hasUnderline
)

/**
 * A clickable text with 1-2 clickable spans.
 * The spans should be enclosed in <annotation> tags:
 * First one should be <annotation tag="clickable1">,
 * and second one should be <annotation tag="clickable2">
 */
@Composable
fun ClickableSpanText(
  modifier: Modifier = Modifier,
  @StringRes textRes: Int,
  onClickSpan1: (() -> Unit),
  onClickSpan2: (() -> Unit)? = null,
  style: TextStyle = LocalTextStyle.current,
  @ColorRes spanColor: Color = Colors.links,
  hasUnderline: Boolean = true
) {
  val fullText = LocalContext.current.getText(textRes)
  val spansInfo = (fullText as? SpannedString)?.getSpansInfo()
  val (span1Start, span1End) = spansInfo?.clickableSpan1 ?: Pair(-1, -1)
  val (span2Start, span2End) = spansInfo?.clickableSpan2 ?: Pair(-1, -1)

  val spanStyle = SpanStyle(
    color = spanColor,
    textDecoration = if (hasUnderline) TextDecoration.Underline else null
  )
  val annotatedString = androidx.compose.ui.text.buildAnnotatedString {
    append(fullText)
    if (span1Start >= 0) {
      addStyle(style = spanStyle, start = span1Start, end = span1End)
    }
    if (span2Start >= 0) {
      addStyle(style = spanStyle, start = span2Start, end = span2End)
    }
  }

  ClickableText(
    text = annotatedString,
    modifier = modifier,
    style = style,
    onClick = { offset ->
      if (span1Start >= 0 && offset in (span1Start..span1End)) {
        onClickSpan1()
      }
      if (span2Start >= 0 && offset in (span2Start..span2End)) {
        onClickSpan2?.invoke()
      }
    }
  )
}
