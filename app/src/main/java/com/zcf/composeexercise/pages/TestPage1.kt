package com.zcf.composeexercise.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavController

@Composable
fun TestPage1(navController: NavController) {
  // val (name, onNameChange) = remember {
  //   mutableStateOf("")
  // }
  val (name, onNameChange) = rememberSaveable {
    mutableStateOf("")
  }
  StatelessNameInput(name = name, onNameChange = onNameChange)
}

@Composable
fun StatelessNameInput(name: String, onNameChange: (String) -> Unit) {
  Column {
    Text("Hello $name")
    OutlinedTextField(
      value = name,
      onValueChange = { onNameChange(it) },
      label = { Text(text = "Name")}
    )
  }
}

