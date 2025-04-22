package com.zcf.composeexercise.todos

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun TodoItem(itemId: Int?, navController: NavController) {
  Text(text = "Todo Item: $itemId")
}
