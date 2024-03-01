package com.zcf.composeexercise.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zcf.composeexercise.widget.AutoSizeText

@Composable
fun StatesPage(navController: NavController) {
    Column {
        // val (name, onNameChange) = remember {
        //   mutableStateOf("")
        // }
        val (name, onNameChange) = rememberSaveable {
            mutableStateOf("")
        }
        StatelessNameInput(name = name, onNameChange = onNameChange)

        FillHeight()

        Box(
            modifier = Modifier
              .width(100.dp)
              .height(50.dp)
              .border(1.dp, Color.Black),
          contentAlignment = Alignment.Center
        ) {
            AutoSizeText(
                text = "A very long very long Name",
                maxLines = 2,
                softWrap = false,
            )
        }

    }
}

@Composable
fun StatelessNameInput(name: String, onNameChange: (String) -> Unit) {
    Column {
        Text("Hello $name")
        OutlinedTextField(
            value = name,
            onValueChange = { onNameChange(it) },
            label = { Text(text = "Name") }
        )
    }
}

@Composable
fun FillHeight() {
    return Row(
        modifier = Modifier.height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
              .width(200.dp)
              .fillMaxHeight()
              .background(Color.Red)
        )
        Column {
            Text("Hello")
            Text("World, a long text, a long text, a long text")
        }
    }
}
