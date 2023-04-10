package data.api

import data.net.*
import data.net.xmlrpc.*
import model.Build
import model.Rpm
import retrofit2.Call
import retrofit2.await

internal class KojiApiImpl(private val service: KojiService = KojiService()) : KojiApi {
    override suspend fun getBuild(id: Int): Build = awaitSuccess(::Build) { service.getBuild(id) }

    override suspend fun getBuild(nvr: String): Build = awaitSuccess(::Build) { service.getBuild(nvr) }
    override suspend fun getBuildRpms(buildId: Int): Collection<Rpm> = awaitSuccess(::Rpms) {
        service.getRpmsForBuild(buildId)
    }

    override suspend fun getRpm(rpmId: Int): Rpm = awaitSuccess(::Rpm) {
        service.getRpm(rpmId)
    }

    override suspend fun getRpm(nvra: String): Rpm = awaitSuccess(::Rpm) {
        service.getRpm(nvra)
    }

    override suspend fun getRpm(name: String, version: String, release: String, arch: String): Rpm = awaitSuccess(::Rpm) { service.getRpm(name, version, release, arch) }

    override suspend fun getRpm(
        name: String,
        version: String,
        release: String,
        arch: String,
        location: String
    ): Rpm =  awaitSuccess(::Rpm) { service.getRpm(name, version, release, arch, location) }

    override suspend fun getBuildrootRpms(buildrootId: Int): Collection<Rpm> = awaitSuccess(
        onSuccess = { Rpms(it, idSelector = "rpm_id") }
    ) {
        service.getBuildrootListing(buildrootId)
    }

    private fun xmlRpcFailure(failure: Failure): Nothing {
        val fault = Fault(failure)
        throw XmlRpcException(fault.toString())
    }

    private suspend fun <T : Any> awaitSuccess(onSuccess: (Success) -> T, block: suspend () -> Call<MethodResponse>): T {
        return block().await().fold(
            onSuccess = onSuccess,
            onFailure = ::xmlRpcFailure
        )
    }
}