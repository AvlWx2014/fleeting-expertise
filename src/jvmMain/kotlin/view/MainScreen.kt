package view

import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import model.Rpm
import model.nvra
import viewmodel.MainScreenViewModel

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    var searchText by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopBarContent(
                searchText = searchText,
                onSearchValueChanged = { searchText = it },
                onSearchTriggered = { viewModel.submit(MainScreenViewModel.Intents.SearchBuild(searchText)) },
                onFilterTriggered = { viewModel.submit(MainScreenViewModel.Intents.FilterArch(it)) }
            )
        },
        content = { ScaffoldContent(state, modifier = Modifier.padding(it)) },
        modifier = Modifier.fillMaxSize().padding(8.dp)
    )
}

@Composable
private fun TopBarContent(
    searchText: String,
    onSearchValueChanged: (String) -> Unit,
    onSearchTriggered: () -> Unit = {},
    onFilterTriggered: (String) -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
    ) {
        Box(modifier = Modifier.weight(0.33f))

        FilterChips(
            "All", "x86_64", "aarch64",
            modifier = Modifier.weight(0.33f),
            onChipSelected = onFilterTriggered
        )

        Search(
            searchText = searchText,
            onSearchValueChanged = onSearchValueChanged,
            onSearchTriggered = onSearchTriggered,
            modifier = Modifier.weight(0.33f)
        )
    }
}

@Composable
private fun Search(searchText: String, onSearchValueChanged: (String) -> Unit, onSearchTriggered: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = searchText,
        singleLine = true,
        leadingIcon = {
            IconButton(
                onClick = onSearchTriggered
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        },
        label = { Text("Build Search") },
        placeholder = { Text("Build NVR") },
        onValueChange = onSearchValueChanged,
        modifier = modifier.onKeyEvent { keyEvent ->
            if (keyEvent isCompletePressOf Key.Enter) {
                onSearchTriggered()
                true
            } else {
                false
            }
        }
    )
}

class FilterChipState(
    label: String = "",
    selected: Boolean = false,
) {
    var label: String by mutableStateOf(label)
    var selected: Boolean by mutableStateOf(selected)

    fun clear() {
        selected = false
    }
}

private fun Collection<FilterChipState>.clearAll() = forEach(FilterChipState::clear)

@Composable
private fun FilterChips(
    vararg labels: String,
    modifier: Modifier = Modifier,
    onChipSelected: (String) -> Unit = {}
) {
    val chips = remember(*labels) {
        labels .map {
        FilterChipState(label = it, selected = it == "All") }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        modifier = modifier
    ) {
        chips.forEach {
            FilterChip(
                selected = it.selected,
                onClick = {
                    chips.clearAll()
                    it.selected = true
                    onChipSelected(it.label)
                },
                label = { Text(it.label) }
            )
        }
    }
}

@Composable
private fun ScaffoldContent(state: MainScreenViewModel.State, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.fetching -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            state.forest.isEmpty() -> Text("Rpms go here.", modifier = Modifier.align(Alignment.Center))
            else -> {
                val scrollState = rememberLazyListState()
                RpmForestView(state.forest, scrollState = scrollState)
                VerticalScrollbar(
                    adapter = ScrollbarAdapter(scrollState = scrollState),
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        }
    }
}

@Composable
private fun RpmItem(rpm: Rpm) {
    Text(rpm.nvra)
}

@Composable
private fun BottomBarContent() {

}