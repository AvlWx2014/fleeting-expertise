@file:JvmName("BuildExtensions")
package model

import kotlinx.datetime.Instant

data class Build(
    val buildId: Int,
    val packageId: Int,
    val name: String,
    val version: String,
    val release: String,
    val epoch: String?,
    val state: BuildState,
    val creationTime: Instant,
    val startTime: Instant,
    val completionTime: Instant,
    val taskId: Int = UNKNOWN,
    val ownerId: Int = UNKNOWN,
    val ownerName: String? = null,
    val volumeId: Int = UNKNOWN,
    val volumeName: String? = null,
    val creationEventId: Int = UNKNOWN,
    val source: String? = null,
    val extra: BuildExtras = BuildExtras(),
    val contentGeneratorId: Int = UNKNOWN,
    val contentGeneratorName: String? = null
) {
    companion object {
        const val UNKNOWN = -1
    }
}

val Build.nvr
    get() = "$name-$version-$release"

// TODO: is{Task,Owner,Volume,CreationEvent,ContentGenerator}Known properties?

