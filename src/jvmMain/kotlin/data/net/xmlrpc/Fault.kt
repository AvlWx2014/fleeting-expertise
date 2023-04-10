package data.net.xmlrpc

data class Fault(
    val code: Int,
    val reason: String
) {
    override fun toString(): String = "($code) $reason"
}
