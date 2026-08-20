package hu.yettel.highwayvignette.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hu.yettel.highwayvignette.domain.model.VignetteOption
import hu.yettel.highwayvignette.ui.ConfirmScreen
import hu.yettel.highwayvignette.ui.CountyConfirmScreen
import hu.yettel.highwayvignette.ui.CountySelectionScreen
import hu.yettel.highwayvignette.ui.HomeScreen
import hu.yettel.highwayvignette.ui.SuccessScreen

object Routes {
    const val HOME = "home"
    const val SUCCESS = "success"
    const val CONFIRM_NATIONAL = "confirm_national/{optionId}/{cost}/{trxFee}/{sum}"
    const val COUNTY_SELECTION = "county_selection"
    const val CONFIRM_COUNTY = "confirm_county/{countyIds}"

    fun confirmNational(optionId: String, cost: Double, trxFee: Double, sum: Double) =
        "confirm_national/$optionId/$cost/$trxFee/$sum"

    fun confirmCounty(countyIds: String) = "confirm_county/$countyIds"
}

@Composable
fun HighwayNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(
                onPurchaseClick = { option -> },
                onCountySelectionClick = { navController.navigate(Routes.COUNTY_SELECTION) }
            )
        }

        composable(Routes.COUNTY_SELECTION) {
            CountySelectionScreen(
                onContinue = { ids -> navController.navigate(Routes.confirmCounty(ids)) }
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
            ConfirmScreen(option = option, onOrderSuccess = { navController.navigate(Routes.SUCCESS) })
        }

        composable(Routes.SUCCESS) {
            SuccessScreen(
                onDone = {
                    navController.popBackStack(Routes.HOME, inclusive = false)
                }
            )
        }
        composable(
            route = Routes.CONFIRM_COUNTY,
            arguments = listOf(navArgument("countyIds") { type = NavType.StringType })
        ) {
            CountyConfirmScreen(onOrderSuccess = { navController.navigate(Routes.SUCCESS) })
        }
    }
}