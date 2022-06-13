package com.zcf.composeexercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.composable
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
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(MainScreenRoutes.values()) { route ->
                Button(onClick = { navController.navigate(route.key) }) {
                    Text(route.displayName)
                }
            }
        }
        
    }
}

enum class MainScreenRoutes(val key: String, val displayName: String) {
    Todos("todos", "Todos LazyColumn"),
    StatesPage("statesPage", "States"),
    SideEffectsPage("sideEffectsPage", "Effects and Croroutine Scopes"),
    CanvasPage("canvasPage", "Canvas"),
    GesturesPage("gesturesPage", "Gestures"),
    CompositionLocal("compositionLocal", "CompositionLocal"),
    ChessPage("chessPage", "Chess"),
    Animation("animation", "Animation"),
    BackdropScaffold("backdropScaffold", "BackdropScaffold"),
    MirrorPage("mirrorPage", "Mirror Effect")
}

fun NavGraphBuilder.mainScreenNavGraph(navController: NavController) {
    navigation(startDestination = "mainScreenHomePage", route = "mainScreen") {
        composable("mainScreenHomePage") {
            MainScreen(navController = navController)
        }
        composable(MainScreenRoutes.Todos.key) {
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

        composable(MainScreenRoutes.StatesPage.key) {
            StatesPage(navController = navController)
        }
        composable(MainScreenRoutes.SideEffectsPage.key) {
            SideEffectsPage(navController = navController)
        }
        composable(MainScreenRoutes.CanvasPage.key) {
            canvasPage(navController = navController)
        }
        composable(MainScreenRoutes.GesturesPage.key) {
            GesturesPage()
        }
        composable(MainScreenRoutes.CompositionLocal.key) {
            CompositionLocalPage()
        }
        composable(MainScreenRoutes.ChessPage.key) {
            ChessPage()
        }
        composable(MainScreenRoutes.Animation.key) {
            animationPage()
        }
        composable(MainScreenRoutes.BackdropScaffold.key) {
            backdropScaffoldPage()
        }
        composable(MainScreenRoutes.MirrorPage.key) {
            MirrorPage()
        }
    }
}