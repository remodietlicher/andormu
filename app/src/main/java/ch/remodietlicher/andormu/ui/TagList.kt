package ch.remodietlicher.andormu.ui

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun TagListPreview() {
    TagList(listOf("Tag1", "Tag2", "Tag3", "Tag4", "Tag5", "Tag6", "Tag7"))
}

@Composable
fun TagList(tags: List<String>) {
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)) {
        items(tags.size) { TagItem(tags[it]) }
    }
}

@Composable
fun TagItem(tag: String) {
    Button(onClick = { /*TODO*/}) { Text(text = tag) }
}
