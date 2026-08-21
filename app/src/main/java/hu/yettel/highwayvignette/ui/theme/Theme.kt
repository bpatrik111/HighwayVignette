package hu.yettel.highwayvignette.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BrandLime = Color(0xFFC6F135)
val BrandNavy = Color(0xFF0B1F3A)
val SurfaceGray = Color(0xFFF4F4F6)
val BorderGray = Color(0xFFE0E0E0)
val NonSelected = Color(0xFFE7ECF3)

private val AppColorScheme = lightColorScheme(
    primary = BrandLime,
    onPrimary = BrandNavy,
    secondary = BrandNavy,
    onSecondary = Color.White,
    background = SurfaceGray,
    surface = Color.White,
    onSurface = BrandNavy,
    error = Color(0xFFD32F2F)
)

@Composable
fun HighwayVignetteTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        content = content
    )
}