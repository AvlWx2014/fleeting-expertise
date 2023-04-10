package model

data class BuildrootListing(
    val buildrootId: Int,
    val rpms: Collection<Rpm>
)

fun BuildrootListing.isEmpty(): Boolean = rpms.isEmpty()
fun BuildrootListing.isNotEmpty(): Boolean = rpms.isNotEmpty()
val BuildrootListing.size: Int
    get() = rpms.size

fun BuildrootListing.forEach(block: (Rpm) -> Unit) = rpms.forEach(block)