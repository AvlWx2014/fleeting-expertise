package model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.repository.RpmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RpmTree(
    root: Rpm,
    private val repository: RpmRepository,
    private val scope: CoroutineScope
) {
    val root = Node(root)

    inner class Node(val value: Rpm, val level: Int = 0) {
        var waiting: Boolean by mutableStateOf(false)
        var expanded: Boolean by mutableStateOf(false)
        var canExpand: Boolean? by mutableStateOf(null)

        var children: Collection<Node> by mutableStateOf(emptySet())

        fun expand() {
            when (canExpand) {
                null -> {
                    waiting = true
                    scope.launch(Dispatchers.IO) {
                        // TODO: this needs to be chunked and emit using a Channel
                        val buildrootRpms = repository.getRpmsInBuildroot(value.buildrootId)

                        withContext(Dispatchers.Main) {
                            children = buildrootRpms.map {
                                Node(it, level + 1)
                            }
                            canExpand = true
                            waiting = false
                            expand()
                        }
                    }
                }
                true -> expanded = expanded xor true
                false -> { }
            }
        }
    }
}