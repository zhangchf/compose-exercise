package com.zcf.composeexercise

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.zcf.composeexercise.ui.theme.ComposeExerciseTheme
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

const val URI_HOST = "https://zcf.com"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            ComposeExerciseTheme {
                Router(navController = navController)
            }
        }

        println("languageTag: ${Locale.current.toLanguageTag()}, " +
                "javaLocale: ${java.util.Locale.getDefault().toLanguageTag()}, " +
                "resourceLocales: ${resources.configuration.locales}, " +
                "resourceLanguageTag: ${resources.configuration.locales[0].toLanguageTag()}")

        testRxJava()
    }
}

fun testRxJava() {
    val source1 = Observable.just(1, 2, 3)
    val source2 = Observable.just(4).delay(1, TimeUnit.SECONDS)

    val observer = Observable.merge(
        Observable.just(4).delay(3, TimeUnit.SECONDS),
        Observable.just(1, 2, 3)
    ).observeOn(AndroidSchedulers.mainThread())

    observer.subscribe {
        println("rxjavaTest $it Thread: $")
    }
}

@Composable
fun Router(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "mainScreen") {
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