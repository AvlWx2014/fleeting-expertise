package data.repository

import model.Build

interface BuildRepository {
    suspend fun get(buildId: Int): Build
    suspend fun get(nvr: String): Build

    companion object {
        operator fun invoke(): BuildRepository = BuildRepositoryImpl()
    }
}

