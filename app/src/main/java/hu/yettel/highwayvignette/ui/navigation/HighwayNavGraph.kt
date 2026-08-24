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
    const val SUCCESS = "success/{itemCount}"
    const val CONFIRM_NATIONAL = "confirm_national/{plate}/{optionId}/{cost}/{trxFee}/{sum}"
    const val COUNTY_SELECTION = "county_selection"
    const val CONFIRM_COUNTY = "confirm_county/{countyIds}"

    fun success(itemCount: Int) = "success/$itemCount"

    fun confirmNational(plate: String, optionId: String, cost: Double, trxFee: Double, sum: Double) =
        "confirm_national/$plate/$optionId/$cost/$trxFee/$sum"

    fun confirmCounty(countyIds: String) = "confirm_county/$countyIds"
}

@Composable
fun HighwayNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(
                onPurchaseClick = { plate, option ->
                    navController.navigateSafe(
                        Routes.confirmNational(plate, option.id, option.cost, option.trxFee, option.sum)
                    )
                },
                onCountySelectionClick = { navController.navigateSafe(Routes.COUNTY_SELECTION) }
            )
        }

        composable(Routes.COUNTY_SELECTION) {
            CountySelectionScreen(
                onBack = { navController.popBackStackSafe() },
                onContinue = { ids -> navController.navigateSafe(Routes.confirmCounty(ids)) }
            )
        }

        composable(
            route = Routes.CONFIRM_NATIONAL,
            arguments = listOf(
                navArgument("plate") { type = NavType.StringType },
                navArgument("optionId") { type = NavType.StringType },
                navArgument("cost") { type = NavType.FloatType },
                navArgument("trxFee") { type = NavType.FloatType },
                navArgument("sum") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val option = VignetteOption(
                id = args.getString("optionId")!!,
                vehicleCategory = "CAR",
                cost = args.getFloat("cost").toDouble(),
                trxFee = args.getFloat("trxFee").toDouble(),
                sum = args.getFloat("sum").toDouble()
            )
            ConfirmScreen(
                plate = args.getString("plate")!!,
                option = option,
                onBack = { navController.popBackStackSafe() },
                onCancel = { navController.popBackStackSafe() },
                onOrderSuccess = { navController.navigateSafe(Routes.success(1)) }
            )
        }

        composable(
            route = Routes.SUCCESS,
            arguments = listOf(navArgument("itemCount") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemCount = backStackEntry.arguments?.getInt("itemCount") ?: 1
            SuccessScreen(
                itemCount = itemCount,
                onDone = {
                    navController.popBackStackSafe(Routes.HOME, inclusive = false)
                }
            )
        }

        composable(
            route = Routes.CONFIRM_COUNTY,
            arguments = listOf(navArgument("countyIds") { type = NavType.StringType })
        ) { backStackEntry ->
            val countyIds = backStackEntry.arguments?.getString("countyIds").orEmpty()
            val itemCount = countyIds.split(",").count { it.isNotBlank() }
            CountyConfirmScreen(
                onBack = { navController.popBackStackSafe() },
                onCancel = { navController.popBackStackSafe() },
                onOrderSuccess = { navController.navigateSafe(Routes.success(itemCount)) }
            )
        }
    }
}