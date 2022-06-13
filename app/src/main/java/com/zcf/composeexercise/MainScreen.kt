package com.zcf.composeexercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.zcf.composeexercise.pages.*
import com.zcf.composeexercise.todos.TodoItem
import com.zcf.composeexercise.todos.TodosListView

@Composable
fun MainScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar {
                Text(text = "Main Screen")
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { navController.navigate(MainScreenRoutes.Todos.route) }) {
                Text("Todos LazyColumn")
            }
            Button(onClick = { navController.navigate(MainScreenRoutes.StatesPage.route) }) {
                Text(text = "States")
            }
            Button(onClick = { navController.navigate(MainScreenRoutes.SideEffectsPage.route) }) {
                Text(text = "Effects and Croroutine Scopes")
            }
            Button(onClick = { navController.navigate(MainScreenRoutes.CanvasPage.route) }) {
                Text(text = "Canvas")
            }
            Button(onClick = { navController.navigate(MainScreenRoutes.GesturesPage.route)}) {
                Text("Gestures")
            }
            Button(onClick = { navController.navigate(MainScreenRoutes.CompositionLocal.route) }) {
                Text("CompositionLocal")
            }
            Button(onClick = { navController.navigate(MainScreenRoutes.ChessPage.route) }) {
                Text("Chess")
            }
            Button(onClick = { navController.navigate(MainScreenRoutes.Animation.route) }) {
                Text("Animation")
            }
            Button(onClick = { navController.navigate(MainScreenRoutes.BackdropScaffold.route) }) {
                Text("BackdropScaffold")
            }
        }
        
    }
}


enum class MainScreenRoutes(val route: String) {
    Root("mainScreen"),
    Todos("todos"),
    TodoItem("todoItem"),
    StatesPage("statesPage"),
    SideEffectsPage("sideEffectsPage"),
    CanvasPage("canvasPage"),
    GesturesPage("gesturesPage"),
    CompositionLocal("compositionLocal"),
    ChessPage("chessPage"),
    Animation("animation"),
    BackdropScaffold("backdropScaffold"),
}

fun NavGraphBuilder.mainScreenNavGraph(navController: NavController) {
    navigation(startDestination = "mainScreenHomePage", route = "mainScreen") {
        composable("mainScreenHomePage") {
            MainScreen(navController = navController)
        }
        composable(MainScreenRoutes.Todos.route) {
            TodosListView(navController = navController)
        }

        composable(
            route = "${MainScreenRoutes.TodoItem.route}/{itemId}",
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

        composable(MainScreenRoutes.StatesPage.route) {
            StatesPage(navController = navController)
        }
        composable(MainScreenRoutes.SideEffectsPage.route) {
            SideEffectsPage(navController = navController)
        }
        composable(MainScreenRoutes.CanvasPage.route) {
            canvasPage(navController = navController)
        }
        composable(MainScreenRoutes.GesturesPage.route) {
            GesturesPage()
        }
        composable(MainScreenRoutes.CompositionLocal.route) {
            CompositionLocalPage()
        }
        composable(MainScreenRoutes.ChessPage.route) {
            ChessPage()
        }
        composable(MainScreenRoutes.Animation.route) {
            animationPage()
        }
        composable(MainScreenRoutes.BackdropScaffold.route) {
            backdropScaffoldPage()
        }
    }
}