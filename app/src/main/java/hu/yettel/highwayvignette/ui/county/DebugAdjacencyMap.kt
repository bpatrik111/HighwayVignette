package hu.yettel.highwayvignette.ui.county

import android.graphics.RectF
import android.graphics.Region
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.pointerInput
import hu.yettel.highwayvignette.domain.model.CountyAdjacency
import hu.yettel.highwayvignette.ui.theme.BrandLime
import hu.yettel.highwayvignette.ui.theme.NonSelected

@Composable
fun HungaryCountyMapAdjacencyDebug(
    shapes: List<CountyShape>,
    modifier: Modifier = Modifier
) {
    var selected by remember { mutableStateOf<String?>(null) }

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
                        .filter { it.svgId != "BUDAPEST" }
                        .firstOrNull { it.debugContainsPoint(originalX, originalY) }
                    tapped?.let { selected = it.svgId }
                }
            }
    ) {
        val scaleFactor = size.width / HUNGARY_MAP_WIDTH
        val selectedId = selected

        scale(scaleFactor, scaleFactor, pivot = Offset.Zero) {
            shapes.forEach { shape ->
                val composePath = shape.path.asComposePath()
                val fillColor = when {
                    shape.svgId == selectedId -> BrandLime
                    selectedId == null -> NonSelected
                    CountyAdjacency.areNeighbors(selectedId, shape.svgId) -> Color.Blue
                    else -> Color.Red
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

private fun CountyShape.debugContainsPoint(x: Int, y: Int): Boolean {
    val bounds = RectF()
    path.computeBounds(bounds, true)
    val region = Region()
    region.setPath(
        path,
        Region(bounds.left.toInt(), bounds.top.toInt(), bounds.right.toInt() + 1, bounds.bottom.toInt() + 1)
    )
    return region.contains(x, y)
}