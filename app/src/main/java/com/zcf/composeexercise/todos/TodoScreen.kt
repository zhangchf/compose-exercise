package com.zcf.composeexercise.todos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zcf.composeexercise.MainScreenRoutes
import com.zcf.composeexercise.todos.BottomNavScreen.*
import com.zcf.composeexercise.todos.viewmodel.TodosViewModel


sealed class BottomNavScreen(val title: String, val icon: ImageVector, val route: String) {
  object TextFieldPage : BottomNavScreen("TextField", Icons.Filled.Person, MainScreenRoutes.StatesPage.route)
  object SnackBarPage : BottomNavScreen("SnackBar", Icons.Filled.Call, MainScreenRoutes.SideEffectsPage.route)
  object CanvasPage : BottomNavScreen("Canvas", Icons.Filled.AccountBox, MainScreenRoutes.CanvasPage.route)
}

val BOTTOM_NAV_ITEMS = listOf(
  TextFieldPage, SnackBarPage, CanvasPage
)

@OptIn(ExperimentalAnimationApi::class)
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
      BottomAppBar(
        modifier = Modifier.clip(RoundedCornerShape(8.dp, 8.dp)),
        cutoutShape = CircleShape
      ) {

        BottomNavigation(
        ) {

          BOTTOM_NAV_ITEMS.forEach { screen ->
            BottomNavigationItem(
              selected = false,
              onClick = { navController.navigate(screen.route) },
              icon = {
                Icon(
                  imageVector = screen.icon,
                  contentDescription = screen.title,
                  modifier = Modifier
                    .padding(4.dp)
                )
              },
              label = {
                Text(text = screen.title)
              }
            )
          }
        }
      }
    },
    floatingActionButton = {
      FloatingActionButton(onClick = {  }) {
        Icon(imageVector = Icons.Default.Create, contentDescription = "Create")
      }
    },
    floatingActionButtonPosition = FabPosition.End,
    isFloatingActionButtonDocked = true
  ) {

    val errorMsg = viewModel.errorMsg.value
    if (errorMsg.isEmpty()) {
      LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp)
      ) {
        items(viewModel.todos) {
          Column {
            var isExpanded by remember {
              mutableStateOf(false)
            }

            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
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

            AnimatedVisibility(visible = isExpanded) {
              Text(text = "Expandable area", modifier = Modifier.padding(32.dp, 0.dp, 16.dp, 8.dp))
            }

            Divider()
          }

        }
      }
    } else {
      Text(text = errorMsg)
    }
  }

}