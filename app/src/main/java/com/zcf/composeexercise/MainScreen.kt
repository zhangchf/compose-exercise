package com.zcf.composeexercise

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.zcf.composeexercise.pages.*
import com.zcf.composeexercise.todos.TodoItem
import com.zcf.composeexercise.todos.TodosListView

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen(navController: NavController) {
  Scaffold(
    topBar = {
      TopAppBar {
        Text(text = stringResource(id = com.zcf.resources.R.string.textFromResourcesModule))
//        Text(text = pluralStringResource(id = R.plurals.ordersCounter, 1000))
      }
    }
  ) { paddingValues ->
//        LazyColumn(
//            modifier = Modifier
//                .padding(paddingValues)
//                .fillMaxSize(),
//            verticalArrangement = Arrangement.SpaceEvenly,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            items(MainScreenRoutes.values()) { route ->
//                Button(onClick = { navController.navigate(route.key) }) {
//                    Text(route.displayName)
//                }
//            }
//        }

    FlowRow(modifier = Modifier.padding(paddingValues)) {
      MainScreenRoutes.entries.forEach { route ->
        Button(
          onClick = { navController.navigate(route.key) },
          modifier = Modifier.padding(horizontal = 4.dp),
          shape = RoundedCornerShape(16.dp)
        ) {
          Text(
            text = route.displayName,
            style = MaterialTheme.typography.button
          )
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
  MirrorPage("mirrorPage", "Mirror Effect"),
  ParallaxPage("parallaxPage", "Parallax"),
  PriceAnalyticsPage("priceAnalyticsPage", "Price Analytics Page"),
  TextsPage("textsPage", "Texts"),
  AnimatedTransitionDialog("animatedTransitionDialog", "Animated Transition Dialog"),
  NegativePaddingsPage("negativePaddings", "Negative Paddings"),
  PhotoHarvestingPage("photoHarvesting", "Photo Harvesting")
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
      arguments = listOf(
        navArgument("itemId") {
          this.type = NavType.IntType
        }
      ),
      deepLinks = listOf(
        navDeepLink {
          this.uriPattern = "$URI_HOST/todoItem/{itemId}"
        }
      )
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
      CanvasPage(navController = navController)
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
      AnimationPage()
    }
    composable(MainScreenRoutes.BackdropScaffold.key) {
      backdropScaffoldPage()
    }
    composable(MainScreenRoutes.MirrorPage.key) {
      MirrorPage()
    }
    composable(MainScreenRoutes.ParallaxPage.key) {
      ParallaxPage()
    }
    composable(MainScreenRoutes.PriceAnalyticsPage.key) {
      PriceAnalyticsView()
    }
    composable(MainScreenRoutes.TextsPage.key) {
      Texts()
    }
    composable(MainScreenRoutes.AnimatedTransitionDialog.key) {
      AnimatedBottomSheetDialogPage()
    }
    composable(MainScreenRoutes.NegativePaddingsPage.key) {
      NegativePaddingsPage()
    }
    composable(MainScreenRoutes.PhotoHarvestingPage.key) {
      PhotoHarvestingPage()
    }
  }
}
