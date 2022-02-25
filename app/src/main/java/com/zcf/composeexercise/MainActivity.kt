package com.zcf.composeexercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
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

@Composable
fun Router(navController: NavHostController) {
    NavHost(navController = navController, startDestination = MainScreenRoutes.Root.route) {
        mainScreenNavGraph(navController = navController)

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