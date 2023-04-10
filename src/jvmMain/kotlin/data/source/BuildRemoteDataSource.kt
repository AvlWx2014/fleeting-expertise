package data.source

import data.api.KojiApi
import model.Build

// TODO: rather than splitting in to Remote and LocalDataSource interfaces, what about
//  interface BuildDataSource with Local and Remote implementations?
//  Pros: consistency between APIs for local and remote
//        otherwise, not sure how I'm going to group these things...
//  Cons: how to represent methods in the base interface that remote or local might not need
//        compared to the other?

class BuildRemoteDataSource(private val api: KojiApi = KojiApi()) {
    suspend fun fetch(buildId: Int): Build = api.getBuild(id = buildId)
    suspend fun fetch(nvr: String): Build = api.getBuild(nvr = nvr)

    suspend fun fetchOrNull(buildId: Int): Build? = try {
        fetch(buildId = buildId)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun fetchOrNull(nvr: String): Build? = try {
        fetch(nvr = nvr)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}