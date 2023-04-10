package model

data class BuildRpms(
    val buildId: Int,
    val rpms: Rpms
)

fun BuildRpms.isEmpty(): Boolean = rpms.isEmpty()
fun BuildRpms.isNotEmpty(): Boolean = rpms.isNotEmpty()
val BuildRpms.size: Int
    get() = rpms.size

fun BuildRpms.forEach(block: (Rpm) -> Unit) = rpms.forEach(block)
