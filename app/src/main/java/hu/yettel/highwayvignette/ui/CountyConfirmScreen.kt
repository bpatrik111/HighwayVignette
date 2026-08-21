package hu.yettel.highwayvignette.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.yettel.highwayvignette.R
import hu.yettel.highwayvignette.domain.model.County
import hu.yettel.highwayvignette.ui.common.ConfirmRow
import hu.yettel.highwayvignette.ui.common.HighwayVignetteTopBar
import hu.yettel.highwayvignette.ui.common.formatHuf
import hu.yettel.highwayvignette.ui.theme.BrandNavy

@Composable
fun CountyConfirmScreen(
    viewModel: CountyConfirmViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onCancel: () -> Unit,
    onOrderSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onOrderSuccess()
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
            Text(stringResource(R.string.confirm_purchase), style = MaterialTheme.typography.titleLarge)
            HorizontalDivider(Modifier.padding(vertical = 12.dp))
            Spacer(Modifier.height(16.dp))

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                ConfirmRow(stringResource(R.string.registration_number), state.plate)
                ConfirmRow(stringResource(R.string.vignette_type), stringResource(R.string.annual_counties))

                HorizontalDivider(Modifier.padding(vertical = 12.dp))

                LazyColumn(Modifier.weight(1f, fill = false)) {
                    items(state.selectedCounties, key = County::id) { county ->
                        ConfirmRow(county.name, formatHuf(state.unitPrice?.cost ?: 0.0))
                    }
                    item {
                        ConfirmRow(
                            stringResource(R.string.system_usage_fee),
                            formatHuf((state.unitPrice?.trxFee ?: 0.0) * state.selectedCounties.size)
                        )
                    }
                }

                HorizontalDivider(Modifier.padding(vertical = 12.dp))

                Text(stringResource(R.string.amount_to_be_paid), style = MaterialTheme.typography.bodyMedium)
                Text(formatHuf(state.total), style = MaterialTheme.typography.headlineSmall)

                state.error?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }

                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = viewModel::confirmOrder,
                    enabled = !state.isSubmitting,
                    colors = ButtonDefaults.buttonColors(containerColor = BrandNavy, contentColor = Color.White),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    if (state.isSubmitting) {
                        CircularProgressIndicator(modifier = Modifier.height(20.dp), color = Color.White)
                    } else {
                        Text(stringResource(R.string.general_next), fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onCancel,
                    enabled = !state.isSubmitting,
                    border = BorderStroke(1.dp, BrandNavy),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandNavy),
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text(stringResource(R.string.general_cancel), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}