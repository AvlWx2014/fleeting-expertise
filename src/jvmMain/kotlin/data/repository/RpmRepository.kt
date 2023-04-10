package data.repository

import model.Rpm
import model.Rpms

interface RpmRepository {
    suspend fun get(id: Int): Rpm
    suspend fun get(nvra: String): Rpm
    suspend fun get(name: String, version: String, release: String, architecture: String): Rpm

    suspend fun getAll(): Rpms

    suspend fun getRpmsForBuild(buildId: Int): Rpms

    suspend fun getRpmsInBuildroot(buildrootId: Int): Rpms

    companion object {
        operator fun invoke(): RpmRepository = RpmRepositoryImpl()
    }
}

