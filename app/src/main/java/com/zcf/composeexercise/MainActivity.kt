package com.zcf.composeexercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.zcf.composeexercise.Pages.Todos
import com.zcf.composeexercise.pages.TestPage1
import com.zcf.composeexercise.pages.TestPage2
import com.zcf.composeexercise.pages.canvasPage
import com.zcf.composeexercise.todos.TodoItem
import com.zcf.composeexercise.todos.TodosListView
import com.zcf.composeexercise.ui.theme.ComposeExerciseTheme
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

enum class Pages(val route: String) {
    Todos("todos"),
    TodoItem("todoItem"),
    TestPage1("testPage1"),
    TestPage2("testPage2"),
    CanvasPage("canvasPage")
}

@Composable
fun Router(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Todos.route) {
        composable("todos") {
            TodosListView(navController = navController)
        }
        composable(
            route = "${Pages.TodoItem.route}/{itemId}",
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
        composable(Pages.TestPage1.route) {
            TestPage1(navController = navController)
        }
        composable(Pages.TestPage2.route) {
            TestPage2(navController = navController)
        }
        composable(Pages.CanvasPage.route) {
            canvasPage(navController = navController)
        }
        //nested navigation graph
        navigation("login", route = "loginFlow") {
            composable("login") {}
            composable("signUp") {}
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