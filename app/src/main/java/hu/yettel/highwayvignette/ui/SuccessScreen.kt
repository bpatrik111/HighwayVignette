package hu.yettel.highwayvignette.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.yettel.highwayvignette.R
import hu.yettel.highwayvignette.ui.theme.BrandLime
import hu.yettel.highwayvignette.ui.theme.BrandNavy

@Composable
fun SuccessScreen(itemCount: Int, onDone: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(BrandLime)
    ) {
        ConfettiOverlay(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter))

        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 64.dp, bottom = 24.dp)
        ) {
            Spacer(Modifier.weight(1f))

            Text(
                pluralStringResource(R.plurals.success_message, itemCount),
                color = BrandNavy,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 35.sp
            )

            Spacer(Modifier.height(16.dp))

            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Image(
                    painter = painterResource(id = R.drawable.success),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onDone,
                colors = ButtonDefaults.buttonColors(containerColor = BrandNavy, contentColor = Color.White),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(stringResource(R.string.general_done), fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(30.dp))
        }
    }
}