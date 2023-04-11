package data.api

import model.Build
import model.BuildrootListing
import model.Rpm
import model.Rpms

interface KojiApi {
    suspend fun getBuild(id: Int): Build
    suspend fun getBuild(nvr: String): Build

    suspend fun getBuildRpms(buildId: Int): Rpms

    suspend fun getRpm(rpmId: Int): Rpm
    suspend fun getRpm(nvra: String): Rpm
    suspend fun getRpm(name: String, version: String, release: String, arch: String): Rpm
    suspend fun getRpm(name: String, version: String, release: String, arch: String, location: String): Rpm

    suspend fun getBuildrootRpms(buildrootId: Int): BuildrootListing

    companion object {
        operator fun invoke(): KojiApi = KojiApiImpl()
    }
}