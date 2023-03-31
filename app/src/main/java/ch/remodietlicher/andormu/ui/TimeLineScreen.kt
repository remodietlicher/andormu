package ch.remodietlicher.andormu.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import ch.remodietlicher.andormu.R
import ch.remodietlicher.andormu.model.TaggedTimerViewModel
import kotlinx.coroutines.launch

private const val TAG = "TimelineScreen"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun TimelineScreen() {
    var deletingTimers by remember { mutableStateOf(false) }
    val viewModel: TaggedTimerViewModel = hiltViewModel()
    val listOfTags: Set<String> by viewModel.listOfTags.collectAsState(initial = emptySet())
    val scope = rememberCoroutineScope()
    var showDialog: Boolean by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

    LaunchedEffect(deletingTimers) {
        if (deletingTimers) {
            viewModel.deleteAllTimers()
            deletingTimers = false
        }
    }

    // show dialog that prompts the user for a name of the tag to insert
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = stringResource(id = R.string.add_tag_dialog_title)) },
            text = {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(text = stringResource(id = R.string.tag_entry_label)) }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch { viewModel.addTag(text) }
                        showDialog = false
                    }
                ) {
                    Text(text = stringResource(id = R.string.add))
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        topBar = { Text(text = stringResource(id = R.string.app_name)) },
        content = {
            Column {
//                ActiveTimers(Scale.HOUR)
                Timeline()
                TagList(listOfTags)
                Button(onClick = { deletingTimers = true }) {
                    Text(text = stringResource(id = R.string.delete_all))
                }
                Button(
                    onClick = { scope.launch { listOfTags.forEach { viewModel.removeTag(it) } } }
                ) {
                    Text(text = stringResource(id = R.string.delete_all_tags))
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_add_24),
                    contentDescription = "Add Tag"
                )
            }
        }
    )
}
