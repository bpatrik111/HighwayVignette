package hu.yettel.highwayvignette.ui.county

import android.graphics.RectF
import android.graphics.Region
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.pointerInput

private const val NON_SELECTABLE_ID = "BUDAPEST"

@Composable
fun HungaryCountyMap(
    shapes: List<CountyShape>,
    selectedSvgIds: Set<String>,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(HUNGARY_MAP_WIDTH / HUNGARY_MAP_HEIGHT)
            .pointerInput(shapes) {
                detectTapGestures { offset ->
                    val scaleFactor = size.width / HUNGARY_MAP_WIDTH
                    val originalX = (offset.x / scaleFactor).toInt()
                    val originalY = (offset.y / scaleFactor).toInt()

                    val tapped = shapes
                        .filter { it.svgId != NON_SELECTABLE_ID }
                        .firstOrNull { shape -> shape.containsPoint(originalX, originalY) }
                    tapped?.let { onToggle(it.svgId) }
                }
            }
    ) {
        val scaleFactor = size.width / HUNGARY_MAP_WIDTH

        scale(scaleFactor, scaleFactor, pivot = Offset.Zero) {
            shapes.forEach { shape ->
                val composePath = shape.path.asComposePath()
                val fillColor = when {
                    shape.svgId == NON_SELECTABLE_ID -> Color(0xFFBFBFBF)
                    shape.svgId in selectedSvgIds -> Color(0xFFC6F135)
                    else -> Color(0xFFE7ECF3)
                }
                drawPath(path = composePath, color = fillColor)
                drawPath(
                    path = composePath,
                    color = Color.White,
                    style = Stroke(width = 1f / scaleFactor)
                )
            }
        }
    }
}

private fun CountyShape.containsPoint(x: Int, y: Int): Boolean {
    val bounds = RectF()
    path.computeBounds(bounds, true)
    val region = Region()
    region.setPath(
        path,
        Region(bounds.left.toInt(), bounds.top.toInt(), bounds.right.toInt() + 1, bounds.bottom.toInt() + 1)
    )
    return region.contains(x, y)
}