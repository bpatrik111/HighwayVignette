package hu.yettel.highwayvignette.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.yettel.highwayvignette.domain.model.OrderLineItem
import hu.yettel.highwayvignette.domain.model.VignetteOption
import hu.yettel.highwayvignette.ui.common.ConfirmRow
import hu.yettel.highwayvignette.ui.common.HighwayVignetteTopBar
import hu.yettel.highwayvignette.ui.theme.BrandNavy

@Composable
fun ConfirmScreen(
    plate: String,
    option: VignetteOption,
    viewModel: ConfirmViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onCancel: () -> Unit,
    onOrderSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        if (state is ConfirmState.Success) onOrderSuccess()
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
            Text("Vásárlás megerősítése", style = MaterialTheme.typography.titleLarge)
            HorizontalDivider(Modifier.padding(vertical = 12.dp))
            Spacer(Modifier.height(16.dp))

            ConfirmRow("Rendszám", plate)
            ConfirmRow("Matrica típusa", NATIONAL_LABELS[option.id] ?: option.id)

            HorizontalDivider(Modifier.padding(vertical = 12.dp))

            ConfirmRow("Ár", formatHuf(option.cost))
            ConfirmRow("Rendszerhasználati díj", formatHuf(option.trxFee))

            HorizontalDivider(Modifier.padding(vertical = 12.dp))

            Text("Fizetendő összeg", style = MaterialTheme.typography.bodyMedium)
            Text(formatHuf(option.sum), style = MaterialTheme.typography.headlineSmall)

            if (state is ConfirmState.Error) {
                Spacer(Modifier.height(8.dp))
                Text((state as ConfirmState.Error).message, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    viewModel.confirmOrder(
                        OrderLineItem(type = option.id, category = option.vehicleCategory, cost = option.cost)
                    )
                },
                enabled = state != ConfirmState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = BrandNavy, contentColor = Color.White),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                if (state == ConfirmState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.height(20.dp), color = Color.White)
                } else {
                    Text("Tovább", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = onCancel,
                enabled = state != ConfirmState.Loading,
                border = BorderStroke(1.dp, BrandNavy),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandNavy),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Mégsem", fontWeight = FontWeight.Bold)
            }
        }
    }
}