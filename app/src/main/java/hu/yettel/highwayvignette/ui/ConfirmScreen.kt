package hu.yettel.highwayvignette.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.yettel.highwayvignette.domain.model.VignetteOption

@Composable
fun ConfirmScreen(option: VignetteOption) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Confirm purchase")
        Text("Type: ${option.id}")
        Text("Cost: ${option.cost} HUF")
        Text("Transaction fee: ${option.trxFee} HUF")
        Text("Total: ${option.sum} HUF")
    }
}