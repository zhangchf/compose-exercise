package com.zcf.composeexercise.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zcf.composeexercise.ui.theme.Colors
import com.zcf.composeexercise.ui.theme.TextStyles
import kotlinx.coroutines.delay
import com.zcf.composeexercise.widget.MaskedTextFieldInputType.Digit
import com.zcf.composeexercise.widget.MaskedTextFieldInputType.LetterOrDigit

// To hide Selections in TextField
val HiddenTextSelectionColors = TextSelectionColors(
  handleColor = Color.Transparent,
  backgroundColor = Companion.Transparent
)

class MaskedVisualTransformation(
  private val maskChar: Char,
  private val maxLength: Int,
  private val isError: Boolean,
  private val isBlinked: Boolean,
  private val textColor: Color = Color.Unspecified,
  private val fillMaskColor: Color = Color.Unspecified,
  private val emptyMaskColor: Color = Color.Unspecified,
  private val errorColor: Color = Color.Unspecified
) : VisualTransformation {
  override fun filter(text: AnnotatedString): TransformedText {
    val annotatedString = buildAnnotatedString {
      append(text)
      if (isError) {
        addStyle(
          style = SpanStyle(color = errorColor),
          start = 0,
          end = text.length
        )
      } else if (text.contains(maskChar)) {
        val indexOf1stMaskChar = text.indexOf(maskChar)
        addStyle(
          style = SpanStyle(color = textColor),
          start = 0,
          end = indexOf1stMaskChar
        )
        addStyle(
          style = SpanStyle(color = if (isBlinked) emptyMaskColor else fillMaskColor),
          start = indexOf1stMaskChar,
          end = indexOf1stMaskChar + 1
        )
        if (indexOf1stMaskChar + 1 < maxLength) {
          addStyle(
            style = SpanStyle(color = emptyMaskColor),
            start = indexOf1stMaskChar + 1,
            end = maxLength
          )
        }
      } else {
        addStyle(
          style = SpanStyle(color = textColor),
          start = 0,
          end = text.length
        )
      }
    }
    return TransformedText(
      text = annotatedString,
      offsetMapping = OffsetMapping.Identity
    )
  }
}

const val DEFAULT_PASSCODE_LENGTH = 6
const val DEFAULT_MASK_CHAR = '\u25A0'
const val MASKED_TEXT_FIELD_TEST_TAG = "MaskedTextFieldTestTag"

enum class MaskedTextFieldInputType {
  Digit, LetterOrDigit
}

@Composable
fun MaskedTextField(
  modifier: Modifier = Modifier,
  inputType: MaskedTextFieldInputType = Digit,
  maxLength: Int = DEFAULT_PASSCODE_LENGTH,
  maskChar: Char = DEFAULT_MASK_CHAR,
  isError: Boolean = false,
  isBlinkable: Boolean = true,
  onValueChange: (String) -> Unit = {},
  onInputComplete: (String) -> Unit = {},
  textColor: Color = Colors.gray3Solid,
  fillMaskColor: Color = Colors.gray3Solid,
  emptyMaskColor: Color = Colors.gray1Solid,
  errorColor: Color = Colors.error,
  backgroundColor: Color = Color.Transparent,
  borderColor: Color = Colors.gray1Solid,
  textStyle: TextStyle = TextStyles.num2Medium(color = Colors.gray3Solid).copy(letterSpacing = 13.sp)
) {
  var textFieldValue by remember {
    mutableStateOf(
      TextFieldValue(
        text = "".padEnd(maxLength, maskChar),
        selection = TextRange(0)
      )
    )
  }
  val focusRequester = remember { FocusRequester() }
  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }
  var isBlinked by remember {
    mutableStateOf(false)
  }
  if (isBlinkable) {
    LaunchedEffect(Unit) {
      while (true) {
        delay(500L)
        isBlinked = !isBlinked
      }
    }
  }
  val focusManager = LocalFocusManager.current
  Box(
    modifier = modifier
      .width(260.dp)
      .height(72.dp)
      .border(width = 1.dp, color = borderColor),
    contentAlignment = Alignment.Center
  ) {
    CompositionLocalProvider(
      LocalTextToolbar provides PasteOnlyTextToolbar(LocalView.current),
      LocalTextSelectionColors provides HiddenTextSelectionColors
    ) {
      TextField(
        value = textFieldValue,
        onValueChange = { value ->
          val newText = value.text
            .filter { c ->
              when (inputType) {
                Digit -> c.isDigit()
                LetterOrDigit -> c.isLetterOrDigit()
              }
            }
          val trimIndex = if (newText.length > maxLength) newText.length - maxLength else 0
          val input = newText.substring(trimIndex, newText.length)
          val changed = input != textFieldValue.text
          textFieldValue = TextFieldValue(
            text = newText.padEnd(maxLength, maskChar),
            selection = TextRange(newText.length)
          )

          if (changed) {
            onValueChange(newText)
            if (newText.length == maxLength) {
              onInputComplete(newText)
            }
          }
        },
        modifier = Modifier
          .testTag(tag = MASKED_TEXT_FIELD_TEST_TAG)
          .width(IntrinsicSize.Min)
          .focusRequester(focusRequester = focusRequester)
          .background(
            color = backgroundColor,
            shape = RectangleShape
          ),
        keyboardOptions = KeyboardOptions(
          capitalization = KeyboardCapitalization.Characters,
          keyboardType = when (inputType) {
            Digit -> KeyboardType.Number
            LetterOrDigit -> KeyboardType.Text
          },
          imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
          onDone = {
            focusManager.clearFocus()
          }
        ),
        singleLine = true,
        textStyle = textStyle.copy(textAlign = TextAlign.Center),
        colors = TextFieldDefaults.textFieldColors(
          textColor = textColor,
          backgroundColor = backgroundColor,
          disabledIndicatorColor = Color.Transparent,
          errorIndicatorColor = Color.Transparent,
          focusedIndicatorColor = Color.Transparent,
          unfocusedIndicatorColor = Color.Transparent,
          cursorColor = Color.Transparent,
          errorCursorColor = Color.Transparent
        ),
        visualTransformation = MaskedVisualTransformation(
          maskChar = maskChar,
          maxLength = maxLength,
          isError = isError,
          isBlinked = isBlinked,
          textColor = textColor,
          fillMaskColor = fillMaskColor,
          emptyMaskColor = emptyMaskColor,
          errorColor = errorColor
        )
      )
    }
  }
}
