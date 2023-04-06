package data.net

import CONFIG_DIR
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import data.net.xmlrpc.XmlFormat
import data.net.xmlrpc.MethodCall
import data.net.xmlrpc.MethodResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.tls.HandshakeCertificates
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.POST
import java.nio.file.Path
import kotlin.io.path.div

// TODO: use OkHttp client directly, since the resource for these annotations
//  needs to be dynamic based on config.
interface KojiService {

    @POST("kojihub")
    fun post(@Body body: MethodCall): Call<MethodResponse>

    companion object {
        private fun configureCertificates(vararg certificates: Path): HandshakeCertificates = HandshakeCertificates.Builder()
            .apply {
                certificates.forEach {
                    addTrustedCertificate(it.readX509Certificate())
                }
            }
            .build()

        private fun configureClient(config: KojiConfig): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .apply {
                // TODO how to handle client cert?
                config.serverCertificateAuthority?.let {
                    val certificates = configureCertificates(Path.of(it))
                    sslSocketFactory(certificates.sslSocketFactory(), certificates.trustManager)
                }
            }
            .build()

        private fun configureBuilder(configPath: Path): Retrofit {
            val config = KojiConfig(configPath)
            return Retrofit.Builder()
                .baseUrl(config.api)
                .client(configureClient(config))
                .addConverterFactory(XmlFormat.asConverterFactory("application/xml".toMediaType()))
                .build()
        }

        operator fun invoke(configPath: Path = CONFIG_DIR / "config.json"): KojiService {
            val builder = configureBuilder(configPath)
            return builder.create()
        }
    }
}