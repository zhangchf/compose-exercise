package com.zcf.composeexercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.NavType.Companion
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.zcf.composeexercise.Pages.Todos
import com.zcf.composeexercise.pages.TestPage1
import com.zcf.composeexercise.pages.TestPage2
import com.zcf.composeexercise.todos.TodoItem
import com.zcf.composeexercise.todos.TodosListView
import com.zcf.composeexercise.ui.theme.ComposeExerciseTheme
import com.zcf.composeexercise.todos.viewmodel.TodosViewModel
import dagger.hilt.android.AndroidEntryPoint

const val URI_HOST = "https://zcf.com"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            ComposeExerciseTheme {
                Router(navController = navController)
            }
        }
    }
}

enum class Pages(val id: String) {
    Todos("todos"),
    TodoItem("todoItem/{itemId}"),
    TestPage1("testPage1"),
    TestPage2("testPage2")
}

@Composable
fun Router(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Todos.id) {
        composable("todos") {
            TodosListView(navController = navController)
        }

        composable(
            route = "todoItem/{itemId}",
            arguments = listOf(navArgument("itemId") {
                this.type = NavType.IntType
            }),
            deepLinks = listOf(navDeepLink {
                this.uriPattern = "$URI_HOST/todoItem/{itemId}"
            })
        ) { backStackEntry ->
            val arguments = backStackEntry.arguments
            TodoItem(itemId = arguments?.getInt("itemId"), navController = navController)
        }

        composable("testPage1") {
            TestPage1(navController = navController)
        }

        composable("testPage2") {
            TestPage2(navController = navController)
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeExerciseTheme {
        Greeting("Android")
    }
}