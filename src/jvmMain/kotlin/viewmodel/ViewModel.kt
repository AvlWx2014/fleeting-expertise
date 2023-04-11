package viewmodel

import kotlinx.coroutines.flow.StateFlow

interface ViewModel<in Intent: Any, out State: Any> {
    val state: StateFlow<State>
    fun submit(intent: Intent)
}