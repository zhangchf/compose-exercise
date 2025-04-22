package com.zcf.composeexercise.widget

import android.text.SpannedString
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

/**
 * A clickable text with 1-2 clickable spans.
 * The spans should be enclosed in <annotation> tags:
 * First one should be <annotation tag="clickable1">,
 * and second one should be <annotation tag="clickable2">
 */
@Composable
fun BulletSpanText(
  modifier: Modifier = Modifier,
  @StringRes textRes: Int,
  bulletGapWidthDp: Int = DEFAULT_BULLET_GAP_WIDTH_DP,
  extraLeadingMarginDp: Int = bulletGapWidthDp
) {
  val fullText = LocalContext.current.getText(textRes)
  val spansInfo = (fullText as? SpannedString)?.getSpansInfo()

  if (spansInfo == null) {
    Text(
      text = fullText.toString(),
      modifier = modifier
    )
    return
  }

  val sections = fullText.getStringSections(spansInfo)

//  val spanStyle = ParagraphStyle(textIndent = TextIndent(firstLine = 20.sp, restLine = 20.sp))
//  var start = 0
//  val annotatedString = buildAnnotatedString {
//    sections.forEach { (section, bulleted) ->
//      append(section)
//      if (bulleted) {
//        addStyle(spanStyle, start, start + section.length)
//      }
//      start += section.length
//    }
//  }
  Column(modifier = modifier) {
    sections.forEach { (section, bulleted) ->
      if (bulleted) {
        Row {
          Text(text = "  \u2022  ")
          Text(text = section)
        }
      } else {
        Text(text = section)
      }
    }
  }
}

private fun SpannedString.getStringSections(spansInfo: SpansInfo): List<Pair<String, Boolean>> {
  val sections = mutableListOf<Pair<String, Boolean>>()
  var previous = 0
  spansInfo.bulletSpans?.forEach { (start, end) ->
    if (start > previous) {
      sections.add(Pair(substring(previous, start), false))
    }
    sections.add(Pair(substring(start, end), true))
    previous = end
  }
  return sections
}
