package com.zcf.composeexercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.zcf.composeexercise.pages.TestPage1
import com.zcf.composeexercise.pages.TestPage2
import com.zcf.composeexercise.pages.canvasPage
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
            Button(onClick = { navController.navigate(MainScreenRoutes.TestPage1.route) }) {
                Text(text = "States")
            }
            Button(onClick = { navController.navigate(MainScreenRoutes.TestPage2.route) }) {
                Text(text = "Effects and Croroutine Scopes")
            }
            Button(onClick = { navController.navigate(MainScreenRoutes.CanvasPage.route) }) {
                Text(text = "Canvas")
            }
            
        }
        
    }
}


enum class MainScreenRoutes(val route: String) {
    Root("mainScreen"),
    Todos("todos"),
    TodoItem("todoItem"),
    TestPage1("testPage1"),
    TestPage2("testPage2"),
    CanvasPage("canvasPage")
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

        composable(MainScreenRoutes.TestPage1.route) {
            TestPage1(navController = navController)
        }
        composable(MainScreenRoutes.TestPage2.route) {
            TestPage2(navController = navController)
        }
        composable(MainScreenRoutes.CanvasPage.route) {
            canvasPage(navController = navController)
        }

        //nested navigation graph
        navigation("login", route = "loginFlow") {
            composable("login") {}
            composable("signUp") {}
        }
    }
}