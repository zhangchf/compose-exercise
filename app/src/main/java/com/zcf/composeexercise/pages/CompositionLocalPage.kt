package com.zcf.composeexercise.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun CompositionLocalPage() {
  Column {
    TestText("Default alpha")
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
      Text("Medium alpha")
      CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
        TestText(text = "Disabled alpha")
      }
    }
  }
}

@Composable
fun TestText(text: String) {
  Text(text)
}
