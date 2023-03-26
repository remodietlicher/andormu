package ch.remodietlicher.andormu.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import ch.remodietlicher.andormu.R
import ch.remodietlicher.andormu.model.TaggedTimerViewModel

private const val TAG = "TimelineScreen"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun TimelineScreen() {
    var deletingTimers by remember { mutableStateOf(false) }
    val viewModel: TaggedTimerViewModel = hiltViewModel()

    LaunchedEffect(deletingTimers) {
        if (deletingTimers) {
            viewModel.deleteAllTimers()
            deletingTimers = false
        }
    }

    Scaffold(
        topBar = { Text(text = stringResource(id = R.string.app_name)) },
        content = {
            Column {
                ActiveTimers(Scale.HOUR)
                TagList(listOf("Tag1", "Tag2", "Tag3", "Tag4", "Tag5", "Tag6", "Tag7"))
                Button(onClick = { deletingTimers = true }) {
                    Text(text = stringResource(id = R.string.delete_all))
                }
            }
        },
        floatingActionButton = { FloatingActionButton(onClick = { /*TODO*/}) {} }
    )
}
