package com.zcf.composeexercise.todos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zcf.composeexercise.Pages
import com.zcf.composeexercise.todos.viewmodel.TodosViewModel

@Composable
fun TodosListView(
  navController: NavController,
  viewModel: TodosViewModel = hiltViewModel()
) {
  LaunchedEffect(key1 = null) {
    viewModel.getTodos()
  }


  Scaffold(
    topBar = {
      TopAppBar() {
        Row {
          Text(text = "Todos")
        }
      }
    },
    bottomBar = {
      BottomAppBar() {
        Row(
          modifier = Modifier.fillMaxSize(),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Filled.AccountBox,
            contentDescription = "Tab 1",
            modifier = Modifier.padding(4.dp).clickable { navController.navigate(Pages.TestPage1.route) }
          )
          Icon(
            imageVector = Icons.Filled.Create,
            contentDescription = "Tab 2",
            modifier = Modifier.padding(4.dp).clickable { navController.navigate(Pages.TestPage2.route) }
          )
          Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = "Canvas",
            modifier = Modifier.padding(4.dp).clickable { navController.navigate(Pages.CanvasPage.route) }
          )
        }

      }
    }

  ) {

    val errorMsg = viewModel.errorMsg.value
    if (errorMsg.isEmpty()) {
      LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp)
      ) {
        items(viewModel.todos) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = it.title,
              modifier = Modifier
                .weight(1f)
                .clickable { navController.navigate("todoItem/${it.id}") }
            )
            Checkbox(
              checked = it.completed,
              onCheckedChange = null,
              modifier = Modifier.padding(start = 16.dp)
            )
          }
        }
      }
    } else {
      Text(text = errorMsg)
    }
  }

}