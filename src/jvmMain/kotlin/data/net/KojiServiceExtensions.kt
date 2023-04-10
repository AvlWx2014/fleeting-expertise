package data.net

import data.net.xmlrpc.MethodResponse
import data.net.xmlrpc.methodCall
import retrofit2.Call

fun KojiService.getBuild(id: Int, strict: Boolean = false): Call<MethodResponse> = post(
    methodCall("getBuild", id, strict)
)

fun KojiService.getBuild(nvr: String, strict: Boolean = false): Call<MethodResponse> = post(
    methodCall("getBuild", nvr, strict)
)

fun KojiService.getBuild(
    name: String,
    version: String,
    release: String,
    strict: Boolean = false
): Call<MethodResponse> = getBuild(
    nvr = "$name-$version-$release",
    strict = strict
)

fun KojiService.getRpmsForBuild(buildId: Int): Call<MethodResponse> = post(
    methodCall("listRPMs", buildId)
)

fun KojiService.getBuildrootListing(buildrootId: Int): Call<MethodResponse> = post(
    methodCall(
        "getBuildrootListing",
        buildrootId
    )
)

fun KojiService.getRpm(id: Int, strict: Boolean = false, multi: Boolean = false): Call<MethodResponse> =
    post(methodCall("getRpm", id, strict, multi))

fun KojiService.getRpm(nvra: String, strict: Boolean = false, multi: Boolean = false): Call<MethodResponse> =
    post(methodCall("getRpm", nvra, strict, multi))

fun KojiService.getRpm(
    name: String,
    version: String,
    release: String,
    arch: String,
    strict: Boolean = false,
    multi: Boolean = false
): Call<MethodResponse> = getRpm(nvra = "$name-$version-${release}.$arch", strict = strict, multi = multi)

fun KojiService.getRpm(
    name: String,
    version: String,
    release: String,
    arch: String,
    location: String,
    strict: Boolean = false,
    multi: Boolean = false
): Call<MethodResponse> = getRpm(nvra = "$name-$version-${release}.$arch@$location", strict = strict, multi = multi)