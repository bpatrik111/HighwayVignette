package hu.yettel.highwayvignette.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VehicleTypeIcon(type: String, modifier: Modifier = Modifier) {
    val icon = when (type.uppercase()) {
        "CAR" -> Icons.Filled.DirectionsCar
        "TRUCK" -> Icons.Filled.LocalShipping
        "MOTORCYCLE" -> Icons.Filled.TwoWheeler
        else -> Icons.AutoMirrored.Filled.Help
    }
    Icon(imageVector = icon, contentDescription = type, modifier = modifier)
}