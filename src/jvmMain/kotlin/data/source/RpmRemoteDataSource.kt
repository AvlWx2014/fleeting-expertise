package data.source

import data.api.KojiApi
import model.Rpm
import model.Rpms

class RpmRemoteDataSource(private val api: KojiApi = KojiApi()) {
    suspend fun get(id: Int): Rpm = api.getRpm(id)

    suspend fun get(nvra: String): Rpm = api.getRpm(nvra)

    suspend fun get(name: String, version: String, release: String, architecture: String): Rpm = get("$name-$version-$release.$architecture")

    suspend fun getRpmsForBuild(buildId: Int): Rpms = api.getBuildRpms(buildId)

    suspend fun getRpmsInBuildroot(buildrootId: Int): Rpms = api.getBuildrootRpms(buildrootId)
}