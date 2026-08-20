package hu.yettel.highwayvignette

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import hu.yettel.highwayvignette.ui.navigation.HighwayNavGraph
import hu.yettel.highwayvignette.ui.theme.HighwayVignetteTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HighwayVignetteTheme {
                HighwayNavGraph()
            }
        }
    }
}