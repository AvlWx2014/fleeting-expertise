package data.repository

import data.source.BuildLocalDataSource
import data.source.BuildRemoteDataSource
import model.Build

internal class BuildRepositoryImpl(
    private val remoteDataSource: BuildRemoteDataSource = BuildRemoteDataSource(),
    private val localDataSource: BuildLocalDataSource = BuildLocalDataSource()
) : BuildRepository {

    override suspend fun get(nvr: String): Build = if (localDataSource.contains(nvr)) {
        localDataSource.get(nvr)
    } else {
        remoteDataSource.fetch(nvr).also { localDataSource.add(it) }
    }
    override suspend fun get(buildId: Int): Build = if (localDataSource.contains(buildId)) {
        localDataSource.get(buildId)
    } else {
        remoteDataSource.fetch(buildId).also { localDataSource.add(it) }
    }
}