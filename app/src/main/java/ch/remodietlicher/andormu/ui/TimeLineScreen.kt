package ch.remodietlicher.andormu.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ch.remodietlicher.andormu.R
import ch.remodietlicher.andormu.model.TaggedTimerViewModel
import kotlinx.coroutines.flow.collect

private const val TAG = "TimelineScreen"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun TimelineScreen() {
    var isDeleting by remember { mutableStateOf(false) }
    var loadingTimers by remember { mutableStateOf(false) }
    val viewModel = remember { TaggedTimerViewModel() }

    LaunchedEffect(isDeleting) {
        if (isDeleting) {
            viewModel.deleteAllTimers()
            isDeleting = false
        }
    }

    LaunchedEffect(loadingTimers) {
        if (loadingTimers) {
            val timers = viewModel.getAllTimers().collect()
            Log.d(TAG, timers.toString())
            loadingTimers = false
        }
    }

    Scaffold(
        topBar = { Text(text = stringResource(id = R.string.app_name)) },
        content = {
            Column {
                ActiveTimers()
                TagList(listOf("Tag1", "Tag2", "Tag3", "Tag4", "Tag5", "Tag6", "Tag7"))
                Button(onClick = { isDeleting = true }) {
                    Text(text = stringResource(id = R.string.delete_all))
                }
                Button(onClick = { loadingTimers = true }) {
                    Text(text = stringResource(id = R.string.load_timers))
                }
            }
        },
        floatingActionButton = { FloatingActionButton(onClick = { /*TODO*/}) {} }
    )
}
