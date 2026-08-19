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
import hu.yettel.highwayvignette.domain.model.OrderLineItem
import hu.yettel.highwayvignette.domain.model.VignetteOption

@Composable
fun ConfirmScreen(
    option: VignetteOption,
    viewModel: ConfirmViewModel = hiltViewModel(),
    onOrderSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        if (state is ConfirmState.Success) onOrderSuccess()
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Confirm purchase")
        Text("Type: ${option.id}")
        Text("Cost: ${option.cost} HUF")
        Text("Transaction fee: ${option.trxFee} HUF")
        Text("Total: ${option.sum} HUF")

        when (val s = state) {
            is ConfirmState.Error -> Text(s.message)
            ConfirmState.Loading -> CircularProgressIndicator()
            else -> {}
        }

        Button(
            onClick = {
                viewModel.confirmOrder(OrderLineItem(type = option.id, category = option.vehicleCategory, cost = option.cost))
            },
            enabled = state != ConfirmState.Loading
        ) {
            Text("Confirm")
        }
    }
}