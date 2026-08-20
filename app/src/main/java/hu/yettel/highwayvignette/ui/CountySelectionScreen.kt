package hu.yettel.highwayvignette.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CountySelectionScreen(
    viewModel: CountySelectionViewModel = hiltViewModel(),
    onContinue: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            state.warning?.let { Text(it) }

            LazyColumn(Modifier.weight(1f)) {
                items(state.counties, key = { it.id }) { county ->
                    Row {
                        Checkbox(
                            checked = county.id in state.selectedIds,
                            onCheckedChange = { viewModel.toggleCounty(county.id) }
                        )
                        Text(county.name)
                    }
                }
            }

            Button(
                onClick = { onContinue(state.selectedIds.joinToString(",")) },
                enabled = state.selectedIds.isNotEmpty()
            ) {
                Text("Continue")
            }
        }
    }
}