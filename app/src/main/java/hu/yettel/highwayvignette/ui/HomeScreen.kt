package hu.yettel.highwayvignette.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        when {
            state.isLoading -> CircularProgressIndicator()
            state.error != null -> Text(state.error ?: "")
            state.vehicle != null -> {
                Text("Plate: ${state.vehicle?.plate}, Owner: ${state.vehicle?.ownerName}")
                Spacer(Modifier.height(16.dp))
                Text("National vignette options:")
                state.nationalOptions.forEach { option ->
                    Text("${option.id}: ${option.sum} HUF")
                }
            }
        }
    }
}