package hu.yettel.highwayvignette.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.yettel.highwayvignette.domain.model.VignetteOption
import hu.yettel.highwayvignette.ui.common.HighwayVignetteTopBar
import hu.yettel.highwayvignette.ui.theme.BorderGray
import hu.yettel.highwayvignette.ui.theme.BrandNavy
import java.text.NumberFormat
import java.util.Locale

private val hunNumberFormat = NumberFormat.getNumberInstance(Locale("hu", "HU"))
fun formatHuf(amount: Double): String = "${hunNumberFormat.format(amount.toLong())} Ft"

private val NATIONAL_LABELS = mapOf(
    "DAY" to "D1 - napi (1 napos)",
    "WEEK" to "D1 - heti (10 napos)",
    "MONTH" to "D1 - havi",
    "YEAR" to "D1 - éves"
)

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onPurchaseClick: (VignetteOption) -> Unit,
    onCountySelectionClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(topBar = { HighwayVignetteTopBar() }) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> Text(state.error ?: "", color = MaterialTheme.colorScheme.error)
                state.vehicle != null -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            VehicleTypeIcon(type = state.vehicle!!.vehicleType, modifier = Modifier.padding(end = 12.dp))
                            Column {
                                Text(state.vehicle!!.plate, style = MaterialTheme.typography.titleMedium)
                                Text(state.vehicle!!.ownerName, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Országos matricák",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Column(Modifier.padding(8.dp)) {
                            state.nationalOptions.forEach { option ->
                                val isSelected = option.id == state.selectedOptionId
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) BrandNavy else BorderGray,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable { viewModel.selectOption(option.id) }
                                        .padding(horizontal = 12.dp, vertical = 14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = { viewModel.selectOption(option.id) },
                                            colors = RadioButtonDefaults.colors(selectedColor = BrandNavy)
                                        )
                                        Text(NATIONAL_LABELS[option.id] ?: option.id)
                                    }
                                    Text(formatHuf(option.sum), style = MaterialTheme.typography.titleMedium)
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    val selectedOption = state.nationalOptions.firstOrNull { it.id == state.selectedOptionId }
                    Button(
                        onClick = { selectedOption?.let(onPurchaseClick) },
                        enabled = selectedOption != null,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandNavy, contentColor = Color.White),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
                        shape = RoundedCornerShape(28.dp),
                        modifier = Modifier.fillMaxWidth().height(56.dp)
                    ) {
                        Text("Vásárlás", fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onCountySelectionClick() },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Éves vármegyei matricák", style = MaterialTheme.typography.titleMedium)
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}