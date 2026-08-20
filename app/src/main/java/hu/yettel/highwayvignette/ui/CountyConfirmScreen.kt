package hu.yettel.highwayvignette.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CountyConfirmScreen(
    viewModel: CountyConfirmViewModel = hiltViewModel(),
    onOrderSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onOrderSuccess()
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Text("Confirm county purchase")
            state.selectedCounties.forEach { county -> Text(county.name) }
            Text("Total: ${state.total} HUF")
            state.error?.let { Text(it) }
            Button(onClick = viewModel::confirmOrder, enabled = !state.isSubmitting) {
                Text("Confirm")
            }
        }
    }
}