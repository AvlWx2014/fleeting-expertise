package data.source

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import model.*

class RpmLocalDataSource {
    private val rpmLock = Mutex()
    private val buildLock = Mutex()
    private val buildrootLock = Mutex()

    private val indexByNvra: MutableMap<String, Rpm> = mutableMapOf()
    private val indexById: MutableMap<Int, Rpm> = mutableMapOf()

    private val indexBuildRpms: MutableMap<Int, Rpms> = mutableMapOf()

    private val indexBuildrootRpms: MutableMap<Int, Rpms> = mutableMapOf()

    suspend fun add(rpm: Rpm) = rpmLock.withLock {
        indexByNvra[rpm.nvra] = rpm
        indexById[rpm.id] = rpm
    }

    suspend fun add(rpms: Rpms) = rpmLock.withLock {
        rpms.forEach {
            indexById[it.id] = it
            indexByNvra[it.nvra] = it
        }
    }

    suspend fun addBuildRpms(buildRpms: BuildRpms) {
        rpmLock.withLock {
            buildRpms.forEach {
                indexById[it.id] = it
                indexByNvra[it.nvra] = it
            }
        }

        buildLock.withLock {
            indexBuildRpms[buildRpms.buildId] = buildRpms.rpms
        }
    }

    suspend fun addBuildrootListing(buildrootListing: BuildrootListing) {
        rpmLock.withLock {
            buildrootListing.forEach {
                indexById[it.id] = it
                indexByNvra[it.nvra] = it
            }
        }

        buildrootLock.withLock {
            indexBuildrootRpms[buildrootListing.buildrootId] = buildrootListing.rpms
        }
    }

    suspend fun contains(id: Int): Boolean = rpmLock.withLock { id in indexById }
    suspend fun contains(nvra: String): Boolean = rpmLock.withLock { nvra in indexByNvra }
    suspend fun containsBuild(buildId: Int): Boolean = buildLock.withLock { buildId in indexBuildRpms }

    suspend fun containsBuildroot(buildrootId: Int): Boolean =
        buildrootLock.withLock { buildrootId in indexBuildrootRpms }

    suspend fun get(id: Int): Rpm = rpmLock.withLock {
        require(id in indexById) {
            "No RPM with id $id"
        }

        // not null assertion OK
        // since both the "require" check and map access
        // are in the critical section we should be reasonably certain
        // the mapping for this key did not change
        indexById[id]!!
    }

    suspend fun get(nvra: String): Rpm = rpmLock.withLock {
        require(nvra in indexByNvra) {
            "No RPM with nvra $nvra"
        }

        // not null assertion OK
        // since both the "require" check and map access
        // are in the critical section we should be reasonably certain
        // the mapping for this key did not change
        indexByNvra[nvra]!!
    }

    suspend fun getAll(): Rpms = rpmLock.withLock { indexByNvra.values }

    suspend fun getBuildRpms(buildId: Int): Rpms = buildLock.withLock {
        require(buildId in indexBuildRpms) {
            "No RPM listing for build $buildId"
        }

        // not null assertion OK
        // since both the "require" check and map access
        // are in the critical section we should be reasonably certain
        // the mapping for this key did not change
        indexBuildRpms[buildId]!!
    }

    suspend fun getBuildrootRpms(buildrootId: Int): Rpms = buildrootLock.withLock {
        require(buildrootId in indexBuildrootRpms) {
            "No buildroot listing for buildroot $buildrootId"
        }

        // not null assertion OK
        // since both the "require" check and map access
        // are in the critical section we should be reasonably certain
        // the mapping for this key did not change
        indexBuildrootRpms[buildrootId]!!
    }

    suspend fun update(oldValue: Rpm, newValue: Rpm) {
        require(oldValue.nvra == newValue.nvra) {
            "Attempted an update operation with mutation to an index value name, version, release, or arch."
        }

        require(oldValue.id == newValue.id) {
            "Attempted an update operation with mutation to index value id."
        }

        rpmLock.withLock {
            indexByNvra[oldValue.nvra] = newValue
            indexById[oldValue.id] = newValue
        }
    }

    suspend fun update(oldValue: Rpm, transform: Rpm.() -> Rpm) = update(oldValue, oldValue.transform())
}