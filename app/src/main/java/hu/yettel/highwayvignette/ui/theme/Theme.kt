package hu.yettel.highwayvignette.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppColorScheme = lightColorScheme(
    primary = BrandLime,
    onPrimary = BrandNavy,
    secondary = BrandNavy,
    onSecondary = Color.White,
    background = SurfaceGray,
    surface = Color.White,
    onSurface = BrandNavy,
    error = Error
)

@Composable
fun HighwayVignetteTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        content = content
    )
}