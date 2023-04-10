package model

import kotlinx.datetime.Instant

typealias Rpms = Collection<Rpm>

data class Rpm(
    val id: Int,
    val name: String,
    val version: String,
    val release: String,
    val arch: String,
    val epoch: String,
    val checksum: String,
    val size: Int,
    val buildId: Int,
    val buildrootId: Int,
    val buildTime: Instant,
    val externalRepoId: Int = 0,
    val externalRepoName: String = INTERNAL_REPO,
    val metadataOnly: Boolean = false,
    val extra: Map<String, Any> = emptyMap()
) {
    companion object {
        const val INTERNAL_REPO = "INTERNAL"
        const val BUILDROOT_UNKNOWN = -1
    }
}

val Rpm.architecture
    get() = arch

val Rpm.nvr
    get() = "$name-$version-$release"

val Rpm.nvra
    get() = "$name-$version-${release}.$arch"