package hu.yettel.highwayvignette.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import hu.yettel.highwayvignette.R
import java.text.NumberFormat
import java.util.Locale

private val hunNumberFormat = NumberFormat.getNumberInstance(Locale("hu", "HU"))

fun formatHuf(amount: Double): String = "${hunNumberFormat.format(amount.toLong())} Ft"

@Composable
fun nationalVignetteLabel(vignetteId: String): String = when (vignetteId) {
    "DAY" -> stringResource(R.string.vignette_day)
    "WEEK" -> stringResource(R.string.vignette_week)
    "MONTH" -> stringResource(R.string.vignette_month)
    "YEAR" -> stringResource(R.string.vignette_year)
    else -> vignetteId
}