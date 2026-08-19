package hu.yettel.highwayvignette.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.yettel.highwayvignette.domain.model.VignetteOption
import hu.yettel.highwayvignette.ui.ConfirmScreen
import hu.yettel.highwayvignette.ui.HomeScreen

object Routes {
    const val HOME = "home"
    const val CONFIRM_NATIONAL = "confirm_national/{optionId}/{cost}/{trxFee}/{sum}"

    fun confirmNational(optionId: String, cost: Double, trxFee: Double, sum: Double) =
        "confirm_national/$optionId/$cost/$trxFee/$sum"
}

@Composable
fun HighwayNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(
                onPurchaseClick = { option ->
                    navController.navigate(
                        Routes.confirmNational(option.id, option.cost, option.trxFee, option.sum)
                    )
                }
            )
        }

        composable(
            route = Routes.CONFIRM_NATIONAL,
            arguments = listOf()
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val option = VignetteOption(
                id = args.getString("optionId")!!,
                vehicleCategory = "CAR",
                cost = args.getFloat("cost").toDouble(),
                trxFee = args.getFloat("trxFee").toDouble(),
                sum = args.getFloat("sum").toDouble()
            )
            ConfirmScreen(option = option, onOrderSuccess = { navController.navigate("success") })
        }

        composable("success") {
            Text("Success! (placeholder)")
        }
    }
}