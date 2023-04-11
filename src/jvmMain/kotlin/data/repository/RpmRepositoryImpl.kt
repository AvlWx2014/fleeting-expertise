package data.repository

import data.source.RpmLocalDataSource
import data.source.RpmRemoteDataSource
import model.Rpm
import model.Rpms

internal class RpmRepositoryImpl(
    private val remoteDataSource: RpmRemoteDataSource = RpmRemoteDataSource(),
    private val localDataSource: RpmLocalDataSource = RpmLocalDataSource()
) : RpmRepository {
    override suspend fun get(id: Int): Rpm = if (localDataSource.contains(id)) {
        localDataSource.get(id)
    } else {
        remoteDataSource.get(id).also { localDataSource.add(it) }
    }

    override suspend fun get(nvra: String): Rpm = if (localDataSource.contains(nvra)) {
        localDataSource.get(nvra)
    } else {
        remoteDataSource.get(nvra).also { localDataSource.add(it) }
    }

    override suspend fun get(name: String, version: String, release: String, architecture: String): Rpm =
        get("$name-$version$-release.$architecture")

    override suspend fun getAll(): Rpms = localDataSource.getAll()

    override suspend fun getRpmsForBuild(buildId: Int): Rpms = if (localDataSource.containsBuild(buildId)) {
            localDataSource.getBuildRpms(buildId)
        } else {
            remoteDataSource.getRpmsForBuild(buildId).also {
                localDataSource.addBuildRpms(buildId, it)
            }
    }

    override suspend fun getRpmsInBuildroot(buildrootId: Int): Rpms = if (localDataSource.containsBuildroot(buildrootId)) {
        localDataSource.getBuildrootRpms(buildrootId)
    } else {
        val buildrootListing = remoteDataSource.getRpmsInBuildroot(buildrootId)
        buildrootListing.map { get(it.id) }.also { localDataSource.addBuildrootListing(buildrootId, it) }
    }
}