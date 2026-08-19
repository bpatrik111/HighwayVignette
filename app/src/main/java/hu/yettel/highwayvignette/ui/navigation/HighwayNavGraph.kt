package hu.yettel.highwayvignette.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.yettel.highwayvignette.ui.HomeScreen

object Routes {
    const val HOME = "home"
    const val CONFIRM_NATIONAL = "confirm_national"
}

@Composable
fun HighwayNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(onPurchaseClick = { navController.navigate(Routes.CONFIRM_NATIONAL) })
        }
        composable(Routes.CONFIRM_NATIONAL) {
            Text("Confirm screen - coming soon")
        }
    }
}