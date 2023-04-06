package data.net

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.nio.file.Path
import kotlin.io.path.inputStream

@Serializable
data class KojiConfig(
    val name: String,
    val hub: String,
    val packages: String,
    val api: String,
    val sslVerify: Boolean = true,
    val clientCert: String? = null,
    val serverCertificateAuthority: String? = null
)

fun KojiConfig(configPath: Path): KojiConfig {
    return configPath.inputStream().use {
        Json.decodeFromStream(it)
    }
}