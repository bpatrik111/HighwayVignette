package hu.yettel.highwayvignette.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.yettel.highwayvignette.ui.theme.BrandLime
import hu.yettel.highwayvignette.ui.theme.BrandNavy

private val TopBarShape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)

@Composable
fun HighwayVignetteTopBar(onBack: (() -> Unit)? = null) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(TopBarShape)
            .background(BrandLime)
            .statusBarsPadding()
            .padding(horizontal = 32.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Vissza", tint = BrandNavy)
            }
        }
        Text(
            "E-matrica",
            color = BrandNavy,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}