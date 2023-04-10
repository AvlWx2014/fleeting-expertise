package data.source

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import model.Build
import model.nvr
import java.lang.IllegalStateException

class BuildLocalDataSource {
    private val lock = Mutex(locked = false)
    private val indexByNvr: MutableMap<String, Build> = mutableMapOf()
    private val indexById: MutableMap<Int, Build> = mutableMapOf()

    suspend fun add(value: Build) {
        lock.withLock {
            indexByNvr[value.nvr] = value
            indexById[value.buildId] = value
        }
    }

    suspend fun contains(nvr: String): Boolean = lock.withLock { nvr in indexByNvr }
    suspend fun contains(buildId: Int): Boolean = lock.withLock { buildId in indexById }

    suspend fun find(predicate: (Build) -> Boolean): Build? = lock.withLock { indexByNvr.values.find(predicate) }

    suspend fun get(buildId: Int): Build = lock.withLock { indexById[buildId] ?: error("No build with id $buildId") }
    suspend fun get(nvr: String): Build = lock.withLock { indexByNvr[nvr] ?: error("No build of NVR $nvr") }

    suspend fun getOrNull(buildId: Int): Build? = try {
        get(buildId = buildId)
    } catch (ise: IllegalStateException) {
        null
    }

    suspend fun getOrNull(nvr: String): Build? = try {
        get(nvr = nvr)
    } catch (ise: IllegalStateException) {
        null
    }

    fun remove(id: String) { indexByNvr -= id }
}