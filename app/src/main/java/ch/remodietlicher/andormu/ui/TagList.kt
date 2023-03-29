package ch.remodietlicher.andormu.ui

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ch.remodietlicher.andormu.model.TaggedTimerViewModel

@Preview
@Composable
fun TagListPreview() {
    TagList(setOf("Programming", "Reading", "Sleeping"))
}

@Composable
fun TagList(tags: Set<String>) {
    val viewModel: TaggedTimerViewModel = hiltViewModel()
    val activeTimerTags: List<String> by viewModel.activeTimerTags.collectAsState(emptyList())

    val tagsList = tags.toList()

    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)) {
        items(tags.size) { TagItem(tagsList[it], activeTimerTags.contains(tagsList[it])) }
    }
}

@Composable
fun TagItem(tag: String, isActive: Boolean = false) {
    val viewModel: TaggedTimerViewModel = hiltViewModel()

    Button(
        onClick = { viewModel.toggleTimer(tag) },
        colors =
            ButtonDefaults.buttonColors(
                backgroundColor =
                    if (isActive) androidx.compose.ui.graphics.Color.Red
                    else androidx.compose.ui.graphics.Color.Green
            )
    ) {
        Text(text = tag)
    }
}
