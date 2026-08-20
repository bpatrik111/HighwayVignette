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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.yettel.highwayvignette.ui.county.CountyShape
import hu.yettel.highwayvignette.ui.county.HungaryCountyMap
import hu.yettel.highwayvignette.ui.county.HungaryCountyShapes

@Composable
fun CountySelectionScreen(
    viewModel: CountySelectionViewModel = hiltViewModel(),
    onContinue: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var shapes by remember { mutableStateOf<List<CountyShape>>(emptyList()) }

    LaunchedEffect(Unit) {
        shapes = HungaryCountyShapes.load(context)
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        if (state.isLoading || shapes.isEmpty()) {
            CircularProgressIndicator()
        } else {
            //HungaryCountyMapAdjacencyDebug(shapes)
            HungaryCountyMap(
                shapes = shapes,
                selectedSvgIds = state.selectedIds,
                onToggle = { countyId -> viewModel.toggleCounty(countyId) }
            )

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