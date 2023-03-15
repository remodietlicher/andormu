package ch.remodietlicher.andormu.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ch.remodietlicher.andormu.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun TimelineScreen() {
    val context = LocalContext.current
    Scaffold(
        topBar = { Text(text = stringResource(id = R.string.app_name)) },
        content = {
            Column {
                ActiveTimers()
                TagList(listOf("Tag1", "Tag2", "Tag3", "Tag4", "Tag5", "Tag6", "Tag7"))
            }
        },
        floatingActionButton = { FloatingActionButton(onClick = { /*TODO*/}) {} }
    )
}
