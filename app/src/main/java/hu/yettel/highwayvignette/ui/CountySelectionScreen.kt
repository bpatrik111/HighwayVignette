package hu.yettel.highwayvignette.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.yettel.highwayvignette.ui.common.HighwayVignetteTopBar
import hu.yettel.highwayvignette.ui.county.CountyShape
import hu.yettel.highwayvignette.ui.county.HungaryCountyMap
import hu.yettel.highwayvignette.ui.county.HungaryCountyShapes
import hu.yettel.highwayvignette.ui.theme.BrandNavy

@Composable
fun CountySelectionScreen(
    viewModel: CountySelectionViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onContinue: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var shapes by remember { mutableStateOf<List<CountyShape>>(emptyList()) }

    LaunchedEffect(Unit) {
        shapes = HungaryCountyShapes.load(context)
    }

    Scaffold(
        containerColor = Color.White,
        topBar = { HighwayVignetteTopBar(onBack = onBack) }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("Éves vármegyei matricák", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

            if (state.isLoading || shapes.isEmpty()) {
                CircularProgressIndicator()
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    HungaryCountyMap(
                        shapes = shapes,
                        selectedSvgIds = state.selectedIds,
                        onToggle = { countyId -> viewModel.toggleCounty(countyId) },
                        modifier = Modifier.padding(8.dp)
                    )
                }

                state.warning?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(Modifier.height(16.dp))

                LazyColumn(Modifier.weight(1f)) {
                    items(state.counties, key = { it.id }) { county ->
                        val isSelected = county.id in state.selectedIds
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = { viewModel.toggleCounty(county.id) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = BrandNavy,
                                        checkmarkColor = Color.White
                                    )
                                )
                                Text(county.name)
                            }
                            state.unitPrice?.let { price ->
                                Text(formatHuf(price.sum), style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }

                HorizontalDivider(Modifier.padding(vertical = 12.dp))
                Spacer(Modifier.height(16.dp))
                Text("Fizetendő összeg", style = MaterialTheme.typography.bodyMedium)
                Text(formatHuf(state.total), style = MaterialTheme.typography.headlineSmall)

                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { onContinue(state.selectedIds.joinToString(",")) },
                    enabled = state.selectedIds.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandNavy, contentColor = Color.White),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("Tovább", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}