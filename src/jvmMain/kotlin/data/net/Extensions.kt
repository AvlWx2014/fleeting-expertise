package data.net

import java.nio.file.Path
import java.security.cert.X509Certificate
import kotlin.io.path.inputStream
import okhttp3.tls.decodeCertificatePem


fun Path.readX509Certificate(): X509Certificate = inputStream()
    .reader()
    .use {
        it.readText().decodeCertificatePem()
    }
