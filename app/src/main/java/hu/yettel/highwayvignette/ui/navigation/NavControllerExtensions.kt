package hu.yettel.highwayvignette.ui.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController

fun NavController.navigateSafe(route: String) {
    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        navigate(route)
    }
}

fun NavController.popBackStackSafe() {
    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        popBackStack()
    }
}

fun NavController.popBackStackSafe(route: String, inclusive: Boolean) {
    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        popBackStack(route, inclusive)
    }
}