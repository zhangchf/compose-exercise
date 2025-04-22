package com.zcf.composeexercise.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zcf.composeexercise.R
import com.zcf.composeexercise.ui.theme.Appearance.MEDIUM
import com.zcf.composeexercise.ui.theme.Appearance.REGULAR
import com.zcf.composeexercise.ui.theme.Colors.gray3Solid

object TextStyles {

  private val fontFamilyMetodo = FontFamily(
    Font(R.font.alias_metodo_regular, weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(R.font.alias_metodo_talic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(R.font.alias_metodo_medium, weight = FontWeight.Medium, style = FontStyle.Normal),
    Font(R.font.alias_metodo_medium_italic, weight = FontWeight.Medium, style = FontStyle.Italic)
  )

  val materialTypography = Typography(
    body1 = bodyRegular(gray3Solid)
  )

  fun big(color: Color) = TextStyle(
    fontFamily = fontFamilyMetodo,
    fontWeight = FontWeight.Medium,
    fontSize = Dimens.Text.big,
    lineHeight = 50.sp,
    color = color
  )

  private fun tiny(
    color: Color,
    appearance: Appearance
  ) = TextStyle(
    fontFamily = fontFamilyMetodo,
    fontWeight = appearance.weight(),
    fontSize = Dimens.Text.tiny,
    lineHeight = 12.sp,
    color = color
  )

  fun tinyRegular(color: Color = Color.Unspecified) = tiny(color, REGULAR)

  private fun small(color: Color, appearance: Appearance) = TextStyle(
    fontFamily = fontFamilyMetodo,
    fontWeight = appearance.weight(),
    fontSize = Dimens.Text.small,
    lineHeight = 20.sp,
    color = color
  )

  fun smallRegular(color: Color = Color.Unspecified) = small(color, REGULAR)
  fun smallMedium(color: Color = Color.Unspecified) = small(color, MEDIUM)

  private fun c1(color: Color, appearance: Appearance) = TextStyle(
    fontFamily = fontFamilyMetodo,
    fontWeight = appearance.weight(),
    fontSize = Dimens.Text.small,
    lineHeight = 20.sp,
    color = color,
    letterSpacing = 1.sp
  )

  fun c1Medium(color: Color = Color.Unspecified) = c1(color, MEDIUM)

  private fun c2(color: Color, appearance: Appearance) = TextStyle(
    fontFamily = fontFamilyMetodo,
    fontWeight = appearance.weight(),
    fontSize = Dimens.Text.tiny,
    lineHeight = 16.sp,
    color = color,
    letterSpacing = 0.5.sp
  )

  fun c2Medium(color: Color = Color.Unspecified) = c2(color, MEDIUM)

  private fun c3(color: Color, appearance: Appearance) = TextStyle(
    fontFamily = fontFamilyMetodo,
    fontWeight = appearance.weight(),
    fontSize = Dimens.Text.Caps4,
    lineHeight = 16.sp,
    color = color,
    letterSpacing = 0.6.sp
  )

  fun c3Medium(color: Color = Color.Unspecified) = c3(color, MEDIUM)

  private fun h1(color: Color, appearance: Appearance) = TextStyle(
    fontFamily = fontFamilyMetodo,
    fontWeight = appearance.weight(),
    fontSize = Dimens.Text.h1,
    lineHeight = 26.sp,
    color = color
  )

  fun h1Regular(color: Color = Color.Unspecified) = h1(color, REGULAR)
  fun h1Medium(color: Color = Color.Unspecified) = h1(color, MEDIUM)

  private fun h2(color: Color, appearance: Appearance) = TextStyle(
    fontFamily = fontFamilyMetodo,
    fontWeight = appearance.weight(),
    fontSize = Dimens.Text.h2,
    lineHeight = 24.sp,
    color = color
  )

  fun h2Regular(color: Color = Color.Unspecified) = h2(color, REGULAR)
  fun h2Medium(color: Color = Color.Unspecified) = h2(color, MEDIUM)

  private fun body(
    color: Color,
    appearance: Appearance,
    textDecoration: TextDecoration = TextDecoration.None,
    textAlign: TextAlign = TextAlign.Unspecified
  ) = TextStyle(
    fontFamily = fontFamilyMetodo,
    fontWeight = appearance.weight(),
    fontSize = Dimens.Text.body,
    lineHeight = 18.sp,
    color = color,
    textDecoration = textDecoration,
    textAlign = textAlign
  )

  fun bodyRegular(
    color: Color = Color.Unspecified,
    textDecoration: TextDecoration = TextDecoration.None,
    textAlign: TextAlign = TextAlign.Unspecified
  ) = body(color, REGULAR, textDecoration, textAlign)

  fun bodyMedium(color: Color = Color.Unspecified) = body(color, MEDIUM)

  fun num4(color: Color = Color.Unspecified) = TextStyle(
    fontFamily = fontFamilyMetodo,
    fontWeight = FontWeight.Medium,
    fontSize = Dimens.Text.num4,
    lineHeight = 20.sp,
    color = color
  )

  fun num2Medium(color: Color = Color.Unspecified) = num2(color, MEDIUM)

  private fun num2(color: Color, appearance: Appearance) = TextStyle(
    fontFamily = fontFamilyMetodo,
    fontWeight = appearance.weight(),
    fontSize = Dimens.Text.num2,
    lineHeight = 40.sp,
    color = color
  )

  fun num1_5(color: Color = Color.Unspecified) = TextStyle(
    fontFamily = fontFamilyMetodo,
    fontWeight = FontWeight.Medium,
    fontSize = Dimens.Text.num1_5,
    color = color
  )

  fun num2_5(color: Color = Color.Unspecified) = TextStyle(
    fontFamily = fontFamilyMetodo,
    fontWeight = FontWeight.Medium,
    fontSize = Dimens.Text.num2_5,
    color = color
  )

  private fun Appearance.weight() = when (this) {
    REGULAR -> FontWeight.Normal
    MEDIUM -> FontWeight.Medium
  }
}

sealed class Appearance {
  object REGULAR : Appearance()
  object MEDIUM : Appearance()
}

@Preview
@Composable
private fun TextStylesPreview() {
  AliasTheme {
    Column {
      Text(
        text = "big",
        style = TextStyles.big(color = gray3Solid)
      )
      Spacer(Modifier.height(10.dp))
      Text(
        text = "tinyRegular",
        style = TextStyles.tinyRegular(color = gray3Solid)
      )
      Spacer(Modifier.height(10.dp))
      Text(
        text = "smallRegular",
        style = TextStyles.smallRegular(color = gray3Solid)
      )
      Spacer(Modifier.height(10.dp))
      Text(
        text = "smallMedium",
        style = TextStyles.smallMedium(color = gray3Solid)
      )
      Spacer(Modifier.height(10.dp))
      Text(
        text = "c1Medium",
        style = TextStyles.c1Medium(color = gray3Solid)
      )
      Spacer(Modifier.height(10.dp))
      Text(
        text = "c2Medium",
        style = TextStyles.c2Medium(color = gray3Solid)
      )
      Spacer(Modifier.height(10.dp))
      Text(
        text = "c3Medium",
        style = TextStyles.c3Medium(color = gray3Solid)
      )
      Spacer(Modifier.height(10.dp))
      Text(
        text = "h1Regular",
        style = TextStyles.h1Regular(color = gray3Solid)
      )
      Spacer(Modifier.height(10.dp))
      Text(
        text = "h1Medium",
        style = TextStyles.h1Medium(color = gray3Solid)
      )
      Spacer(Modifier.height(10.dp))
      Text(
        text = "h2Regular",
        style = TextStyles.h2Regular(color = gray3Solid)
      )
      Spacer(Modifier.height(10.dp))
      Text(
        text = "h2Medium",
        style = TextStyles.h2Medium(color = gray3Solid)
      )
      Spacer(Modifier.height(10.dp))
      Text(
        text = "bodyRegular",
        style = TextStyles.bodyRegular(color = gray3Solid)
      )
      Spacer(Modifier.height(10.dp))
      Text(
        text = "bodyMedium",
        style = TextStyles.bodyMedium(color = gray3Solid)
      )
      Spacer(Modifier.height(10.dp))
      Text(
        text = "num4",
        style = TextStyles.num4(color = gray3Solid)
      )
      Spacer(Modifier.height(10.dp))
      Text(
        text = "num2Medium",
        style = TextStyles.num2Medium(color = gray3Solid)
      )
      Spacer(Modifier.height(10.dp))
      Text(
        text = "num1_5",
        style = TextStyles.num1_5(color = gray3Solid)
      )
      Spacer(Modifier.height(10.dp))
      Text(
        text = "num2_5",
        style = TextStyles.num2_5(color = gray3Solid)
      )
    }
  }
}
