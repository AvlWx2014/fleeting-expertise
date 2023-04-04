import java.io.File
import java.io.InputStream
import java.security.MessageDigest

fun <T : Any> T.resource(path: String): File = this::class.java.getResource(path)?.let { File(it.path) } ?: error("No resource $path")
fun <T : Any> T.loadResource(path: String): InputStream = this::class.java.getResourceAsStream(path)!!

fun ByteArray.toHexString(): String = joinToString(separator="") {
    "%02x".format(it)
}

fun File.hash(algo: String): String {
    val digest = MessageDigest.getInstance(algo)
    inputStream().buffered().use {
        digest.update(it.readBytes())
    }
    return digest.digest().toHexString()
}

fun File.sha256(): String = hash("SHA-256")
fun File.sha512(): String = hash("SHA-512")

fun ByteArray.hash(algo: String): String = MessageDigest.getInstance(algo).digest(this).toHexString()
fun ByteArray.sha256(): String = hash("SHA-256")
fun ByteArray.sha512(): String = hash("SHA-512")

fun String.hash(algo: String): String = toByteArray().hash(algo)
fun String.sha256(): String = hash("SHA-256")
fun String.sha512(): String = hash("SHA-512")