package hu.yettel.highwayvignette.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import hu.yettel.highwayvignette.ui.theme.Particle_1
import hu.yettel.highwayvignette.ui.theme.Particle_2
import hu.yettel.highwayvignette.ui.theme.Particle_3
import hu.yettel.highwayvignette.ui.theme.Particle_4
import kotlin.random.Random

private data class ConfettiPiece(
    val xFraction: Float,
    val yFraction: Float,
    val sizeDp: Float,
    val fallSpeed: Float,
    val initialRotation: Float,
    val rotationSpeed: Float,
    val color: Color
)

private val ConfettiColors = listOf(
    Particle_1,
    Particle_2,
    Particle_3,
    Particle_4
)

private val FadeZoneHeight = 60.dp

@Composable
fun ConfettiOverlay(modifier: Modifier = Modifier, pieceCount: Int = 30) {
    val pieces = remember {
        List(pieceCount) {
            ConfettiPiece(
                xFraction = Random.nextFloat(),
                yFraction = Random.nextFloat(),
                sizeDp = Random.nextInt(4, 10).toFloat(),
                fallSpeed = Random.nextInt(1, 4).toFloat(),
                initialRotation = Random.nextFloat() * 360f,
                rotationSpeed = Random.nextInt(1, 4).toFloat() * 360f * (if (Random.nextBoolean()) 1f else -1f),
                color = ConfettiColors[Random.nextInt(ConfettiColors.size)]
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "confettiProgress"
    )

    Canvas(modifier = modifier.fillMaxWidth().height(300.dp)) {
        val fadeZonePx = FadeZoneHeight.toPx()
        val fadeZoneStart = size.height - fadeZonePx

        pieces.forEach { piece ->
            val x = piece.xFraction * size.width
            val y = ((piece.yFraction + progress * piece.fallSpeed) % 1f) * size.height
            val pieceSizePx = piece.sizeDp.dp.toPx()
            val rotation = piece.initialRotation + progress * piece.rotationSpeed

            val alpha = if (y > fadeZoneStart) {
                (1f - (y - fadeZoneStart) / fadeZonePx).coerceIn(0f, 1f)
            } else {
                1f
            }

            if (alpha > 0f) {
                rotate(degrees = rotation, pivot = Offset(x, y)) {
                    drawRect(
                        color = piece.color.copy(alpha = alpha),
                        topLeft = Offset(x, y),
                        size = Size(pieceSizePx, pieceSizePx * 0.6f)
                    )
                }
            }
        }
    }
}