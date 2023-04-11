package view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.Rpm
import model.RpmForest
import model.RpmTree
import model.nvra

@Composable
fun RpmForestView(
    model: RpmForest,
    scrollState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier
) {
    val nodes = mutableStateListOf<RpmTree.Node>().apply { addAll(model.map { it.root }) }

    LazyColumn(state = scrollState, modifier = modifier) {
        itemsIndexed(nodes) { idx: Int, node: RpmTree.Node ->
            val children = node.children
            RpmNodeView(
                model = node,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(start = 16.dp * node.level)
            ) {
                // TODO: make node.expand return Deferred so this can be awaited
                //  from the application scope, and get application scope in here
                node.expand()
                if (node.expanded) {
                    nodes.addAll(idx + 1, node.children)
                } else {
                    if (node.children.isNotEmpty()) {
                        nodes.removeRange(idx + 1, node.children.size)
                    }
                }
            }
        }
    }
}

@Composable
fun RpmTreeView(model: RpmTree, modifier: Modifier = Modifier) {

}

@Composable
fun RpmNodeView(model: RpmTree.Node, modifier: Modifier = Modifier, onExpand: () -> Unit = {}) {
    val children = model.children

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when {
            model.waiting -> CircularProgressIndicator(modifier = Modifier.weight(0.1f).requiredSize(32.dp))
            model.canExpand != false -> {
                IconButton(
                    onClick = onExpand,
                    modifier = Modifier.weight(0.1f).requiredSize(32.dp),
                ) {
                    val icon = if (model.expanded) {
                        Icons.Default.ArrowDropDown
                    } else {
                        Icons.Default.ArrowRight
                    }

                    Icon(icon, contentDescription = "Toggle Expanded")
                }
            }
            else -> Box(modifier = Modifier.weight(0.1f).requiredSize(32.dp))
        }

        Text(model.value.nvra, modifier = Modifier.weight(0.9f))
    }
}