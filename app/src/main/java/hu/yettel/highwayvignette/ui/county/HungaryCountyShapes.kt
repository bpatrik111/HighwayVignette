package hu.yettel.highwayvignette.ui.county

import android.content.Context
import android.graphics.Path as AndroidPath
import androidx.core.graphics.PathParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

const val HUNGARY_MAP_WIDTH = 1209.727f
const val HUNGARY_MAP_HEIGHT = 746.2031f
const val HUNGARY_COUNTIES_SVG = "hungary_counties.svg"

data class CountyShape(
    val svgId: String,
    val title: String,
    val path: AndroidPath
)

object HungaryCountyShapes {

    suspend fun load(context: Context): List<CountyShape> = withContext(Dispatchers.IO) {
        val shapes = mutableListOf<CountyShape>()

        context.assets.open(HUNGARY_COUNTIES_SVG).use { input ->
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(input, "UTF-8")

            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && parser.name == "path") {
                    val d = parser.getAttributeValue(null, "d")
                    val title = parser.getAttributeValue(null, "title")
                    val id = parser.getAttributeValue(null, "id")
                    if (d != null && title != null && id != null) {
                        shapes += CountyShape(
                            svgId = id,
                            title = title,
                            path = PathParser.createPathFromPathData(d)
                        )
                    }
                }
                eventType = parser.next()
            }
        }

        shapes
    }
}