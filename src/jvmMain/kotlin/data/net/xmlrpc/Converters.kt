package data.net.xmlrpc

import data.net.xmlrpc.BuildrootRpm
import data.net.xmlrpc.Rpm
import kotlinx.datetime.Instant
import model.*
import model.Rpm.Companion.BUILDROOT_UNKNOWN
import nl.adaptivity.xmlutil.core.impl.multiplatform.name

internal fun Build(success: Success): Build {
    // TODO: lift to Success::unwrap and take ValueType as parameter?
    val struct = success.underlying
    checkStruct(struct)
    return Build(
        buildId = struct.int("build_id"),
        packageId = struct.int("package_id"),
        name = struct.string("package_name"),
        version = struct.string("version"),
        release = struct.string("release"),
        epoch = struct.stringOrNull("epoch"),
        state = BuildState[struct.int("state")],
        creationTime = Instant.fromEpochSeconds(struct.double("creation_ts").toLong()),
        startTime = Instant.fromEpochSeconds(struct.double("start_ts").toLong()),
        completionTime = Instant.fromEpochSeconds(struct.double("completion_ts").toLong())
    )
}

internal fun Rpm(success: Success, idSelector: String = "id"): Rpm {
    val struct = success.underlying
    checkStruct(struct)
    return Rpm(struct, idSelector)
}

internal fun Rpm(struct: StructType, idSelector: String = "id"): Rpm = Rpm(
    id = struct.int(idSelector),
    name = struct.string("name"),
    version = struct.string("version"),
    release = struct.string("release"),
    arch = struct.string("arch"),
    epoch = struct.stringOrNull("epoch") ?: "",
    checksum = struct.string("payloadhash"),
    size = struct.int("size"),
    buildId = struct.int("build_id"),
    buildrootId = struct.intOrNull("buildroot_id") ?: BUILDROOT_UNKNOWN,
    buildTime = Instant.fromEpochSeconds(struct.int("buildtime").toLong()),
    externalRepoId = struct.int("external_repo_id"),
    externalRepoName = struct.string("external_repo_name"),
    metadataOnly = struct.boolean("metadata_only")
)

internal fun Rpms(success: Success, idSelector: String = "id"): Collection<Rpm> {
    val array = success.underlying
    checkArray(array)

    return array.map {
        val wrapped = it.value
        checkStruct(wrapped)
        Rpm(wrapped, idSelector)
    }
}

internal fun BuildrootRpm(success: Success): BuildrootRpm {
    val struct = success.underlying
    checkStruct(struct)
    return BuildrootRpm(struct)
}

internal fun BuildrootRpm(struct: StructType): BuildrootRpm = BuildrootRpm (
    id = struct.int("rpm_id"),
    name = struct.string("name"),
    version = struct.string("version"),
    release = struct.string("release"),
    arch = struct.string("arch")
)

internal fun BuildrootListing(buildrootId: Int, success: Success): BuildrootListing {
    val array = success.underlying
    checkArray(array)
    return BuildrootListing(
        buildrootId = buildrootId,
        rpms = array.map {
            val wrapped = it.value
            checkStruct(wrapped)
            BuildrootRpm(wrapped)
        }
    )
}

internal fun Fault(failure: Failure): Fault {
    val struct = failure.underlying
    checkStruct(struct)
    return Fault(
        code = struct.int("faultCode"),
        reason = struct.string("faultString")
    )
}