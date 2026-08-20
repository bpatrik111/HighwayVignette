package hu.yettel.highwayvignette.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.yettel.highwayvignette.domain.model.VignetteOption

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onPurchaseClick: (VignetteOption) -> Unit,
    onCountySelectionClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        when {
            state.isLoading -> CircularProgressIndicator()
            state.error != null -> Text(state.error ?: "")
            state.vehicle != null -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    VehicleTypeIcon(type = state.vehicle!!.vehicleType, modifier = Modifier.padding(end = 8.dp))
                    Column {
                        Text(state.vehicle!!.plate)
                        Text(state.vehicle!!.ownerName)                     }
                }
                Spacer(Modifier.height(16.dp))
                Text("National vignette options:")
                state.nationalOptions.forEach { option ->
                    val isSelected = option.id == state.selectedOptionId
                    Text(
                        text = "${if (isSelected) "> " else "  "}${option.id}: ${option.sum} HUF",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.selectOption(option.id) }
                            .padding(vertical = 8.dp)
                    )
                }
                Spacer(Modifier.height(16.dp))
                val selectedOption = state.nationalOptions.firstOrNull { it.id == state.selectedOptionId }
                Button(
                    onClick = { selectedOption?.let(onPurchaseClick) },
                    enabled = selectedOption != null
                ) {
                    Text("Purchase")
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "County vignettes",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCountySelectionClick() }
                        .padding(vertical = 8.dp)
                )
            }
        }
    }
}