package com.zcf.composeexercise.pages

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun SideEffectsPage(navController: NavController, scaffoldState: ScaffoldState = rememberScaffoldState()) {
  LaunchedEffect(key1 = scaffoldState.snackbarHostState) {
    scaffoldState.snackbarHostState.showSnackbar("Hello, I'm LaunchedEffect!", "click")
  }

  val coroutineScope = rememberCoroutineScope()

  Scaffold(scaffoldState = scaffoldState) {
    Button(onClick = {
      coroutineScope.launch {
        val snackbarResult = scaffoldState.snackbarHostState.showSnackbar("You clicked the button", "button")
        println("zcf snackbarResult: $snackbarResult")
      }
    }) {
      Text(text = "Click me")
    }
  }
}
