package ch.remodietlicher.andormu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ch.remodietlicher.andormu.ui.TimelineScreen
import ch.remodietlicher.andormu.ui.theme.AndormuTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AndormuTheme { TimelineScreen() } }
    }
}
