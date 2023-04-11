package model

typealias BuildrootRpms = Collection<BuildrootRpm>

data class BuildrootListing(
    val buildrootId: Int,
    val rpms: BuildrootRpms
) : Collection<BuildrootRpm> by rpms