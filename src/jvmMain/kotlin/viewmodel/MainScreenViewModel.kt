package viewmodel

import data.repository.BuildRepository
import data.repository.RpmRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import model.*

class MainScreenViewModel(
    private val buildRepository: BuildRepository = BuildRepository(),
    private val rpmRepository: RpmRepository = RpmRepository(),
    parentScope: CoroutineScope
) : ViewModel<MainScreenViewModel.Intents, MainScreenViewModel.State> {

    private val scope: CoroutineScope = parentScope + SupervisorJob(parent = parentScope.coroutineContext[Job]).apply {
        invokeOnCompletion { println("Completing ViewModel SupervisorJob...") }
    }

    private val _state: MutableStateFlow<State> = MutableStateFlow(State())
    override val state: StateFlow<State>
        get() = _state

    private val nvrRegex = ".?-.?-.?".toRegex()

    override fun submit(intent: Intents) {
        when (intent) {
            is Intents.ValidateSearchText -> if (!nvrRegex.matches(intent.text)) {
                transform {
                    copy(searchFailure = "Malformed NVR")
                }
            }

            is Intents.SearchBuild -> {
                transform {
                    copy(fetching = true)
                }

                scope.launch {
                    val build = buildRepository.get(intent.nvr)
                    val buildRpms = rpmRepository.getRpmsForBuild(build.buildId)
                    val forest = RpmForest(
                        buildRpms.map {
                            RpmTree(root = it, repository = rpmRepository, scope = scope)
                        }
                    )
                    withContext(Dispatchers.Main) {
                        transform {
                            copy(fetching = false, forest = forest, buildId = build.buildId)
                        }
                    }
                }
            }

            is Intents.FilterArch -> {
                // if we haven't searched a build yet don't bother
                if (state.value.buildId == Build.UNKNOWN) {
                    return
                }

                transform {
                    copy(fetching = true)
                }

                scope.launch(Dispatchers.IO) {
                    val buildRpms = rpmRepository.getRpmsForBuild(state.value.buildId)
                    val filtered = when (val a = intent.arch) {
                        "All" -> buildRpms
                        else -> buildRpms.filter { it.arch == a }
                    }
                    val forest = RpmForest(
                        filtered.map { RpmTree(root = it, repository = rpmRepository, scope = scope) }
                    )
                    withContext(Dispatchers.Main) {
                        transform {
                            copy(forest = forest, fetching = false)
                        }
                    }
                }
            }

            is Intents.FetchBuildroot -> { /* no-op, handled by RpmTree */ }
        }
    }

    private fun transform(block: State.() -> State) = _state.update(block)

    data class State(
        val buildId: Int = Build.UNKNOWN,
        val forest: RpmForest = RpmForest(),
        val fetching: Boolean = false,
        val searchFailure: String? = null,
    )

    sealed class Intents {
        data class SearchBuild(val nvr: String) : Intents()
        data class FilterArch(val arch: String) : Intents()
        data class FetchBuildroot(val rpm: Rpm) : Intents()
        data class ValidateSearchText(val text: String) : Intents()
    }
}