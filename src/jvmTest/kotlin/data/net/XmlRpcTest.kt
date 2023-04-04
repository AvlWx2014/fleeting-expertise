package data.net

import data.net.xmlrpc.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import loadResource
import resource
import sha256
import sha512
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class XmlRpcTest {

    @Test
    fun testSerializationOfMethodResponse() {
        val response = MethodResponse(
            Success(
                parameterOf(
                    mapOf(
                        "build_id" to 2379863,
                        "epoch" to null,
                        "id" to 2379863,
                        "name" to "curl",
                        "nvr" to "curl-7.76.1-23.el9",
                        "release" to "23.el9",
                        "version" to "7.76.1",
                    )
                )
            )
        )

        val actual = XmlFormat.encodeToString(response)
        assertEquals(resource("/methodResponse-getBuild-Success.xml").sha256(), actual.sha256())
    }

    @Test
    fun testDeserializationOfMethodResponse() {
        val response = loadResource("/methodResponse-getBuild-Success.xml").reader().use {
            XmlFormat.decodeFromString<MethodResponse>(it.readText())
        }
        val expected = MethodResponse(
            Success(
                parameterOf(
                    mapOf(
                        "build_id" to 2379863,
                        "epoch" to null,
                        "id" to 2379863,
                        "name" to "curl",
                        "nvr" to "curl-7.76.1-23.el9",
                        "release" to "23.el9",
                        "version" to "7.76.1",
                    )
                )
            )
        )
        assertEquals(expected, response, "Deserialized MethodResponse did not match expected")
    }

    @Test
    fun testSerializationOfMethodResponseFault() {
        val response = MethodResponse(
            Failure(
                valueOf(
                    mapOf(
                        "faultCode" to 1000,
                        "faultString" to "No such build: 'curl-7.76.1-99.el9'"
                    )
                )
            )
        )

        val actual = XmlFormat.encodeToString(response)
        assertEquals(resource("/methodResponse-getBuild-Fault.xml").sha256(), actual.sha256())
    }

    @Test
    fun testDeserializationOfMethodResponseFault() {
        val response = loadResource("/methodResponse-getBuild-Fault.xml").reader().use {
            XmlFormat.decodeFromString<MethodResponse>(it.readText())
        }
        val expected = MethodResponse(
            Failure(
                valueOf(
                    mapOf(
                        "faultCode" to 1000,
                        "faultString" to "No such build: 'curl-7.76.1-99.el9'"
                    )
                )
            )
        )
        assertEquals(expected, response, "Deserialized MethodResponse did not match expected")
    }

    @Test
    fun testSerializattionOfMethodResponseArrayData() {
        val response = MethodResponse(
            Success(
                parameterOf(
                    listOf(
                        mapOf(
                            "arch" to "src",
                            "build_id" to 2379863,
                            "buildroot_id" to 9721274,
                            "buildtime" to 1676560720,
                            "epoch" to null,
                            "external_repo_id" to 0,
                            "external_repo_name" to "INTERNAL",
                            "extra" to null,
                            "id" to 11958642,
                            "metadata_only" to false,
                            "name" to "curl",
                            "nvr" to "curl-7.76.1-23.el9",
                            "payloadhash" to "4a6f02637874dae959d5cc34a929ff18",
                            "release" to "23.el9",
                            "size" to 2511847,
                            "version" to "7.76.1"
                        ),
                        mapOf(
                            "arch" to "aarch64",
                            "build_id" to 2379863,
                            "buildroot_id" to 9721274,
                            "buildtime" to 1676560726,
                            "epoch" to null,
                            "external_repo_id" to 0,
                            "external_repo_name" to "INTERNAL",
                            "extra" to null,
                            "id" to 11958643,
                            "metadata_only" to false,
                            "name" to "curl-minimal-debuginfo",
                            "nvr" to "curl-minimal-debuginfo-7.76.1-23.el9",
                            "payloadhash" to "8794ad049968a0a4184bd0b24e57806e",
                            "release" to "23.el9",
                            "size" to 153209,
                            "version" to "7.76.1"
                        ),
                        mapOf(
                            "arch" to "aarch64",
                            "build_id" to 2379863,
                            "buildroot_id" to 9721274,
                            "buildtime" to 1676560726,
                            "epoch" to null,
                            "external_repo_id" to 0,
                            "external_repo_name" to "INTERNAL",
                            "extra" to null,
                            "id" to 11958644,
                            "metadata_only" to false,
                            "name" to "libcurl-minimal",
                            "nvr" to "libcurl-minimal-7.76.1-23.el9",
                            "payloadhash" to "226428d3e656bf6d8e93c640b0cbe4ed",
                            "release" to "23.el9",
                            "size" to 226644,
                            "version" to "7.76.1"
                        ),
                    )
                )
            )
        )

        val xml = XmlFormat.encodeToString(response)
        val expected = resource("/methodResponse-getRpmsForBuild-Success.xml")

        assertEquals(expected.sha256(), xml.sha256())
    }

    @Test
    fun testDeserializationOfMethodResponseArrayData() {
        val response = loadResource("/methodResponse-getRpmsForBuild-Success.xml").reader().use {
            XmlFormat.decodeFromString<MethodResponse>(it.readText())
        }

        val expected = MethodResponse(
            Success(
                parameterOf(
                    listOf(
                        mapOf(
                            "arch" to "src",
                            "build_id" to 2379863,
                            "buildroot_id" to 9721274,
                            "buildtime" to 1676560720,
                            "epoch" to null,
                            "external_repo_id" to 0,
                            "external_repo_name" to "INTERNAL",
                            "extra" to null,
                            "id" to 11958642,
                            "metadata_only" to false,
                            "name" to "curl",
                            "nvr" to "curl-7.76.1-23.el9",
                            "payloadhash" to "4a6f02637874dae959d5cc34a929ff18",
                            "release" to "23.el9",
                            "size" to 2511847,
                            "version" to "7.76.1"
                        ),
                        mapOf(
                            "arch" to "aarch64",
                            "build_id" to 2379863,
                            "buildroot_id" to 9721274,
                            "buildtime" to 1676560726,
                            "epoch" to null,
                            "external_repo_id" to 0,
                            "external_repo_name" to "INTERNAL",
                            "extra" to null,
                            "id" to 11958643,
                            "metadata_only" to false,
                            "name" to "curl-minimal-debuginfo",
                            "nvr" to "curl-minimal-debuginfo-7.76.1-23.el9",
                            "payloadhash" to "8794ad049968a0a4184bd0b24e57806e",
                            "release" to "23.el9",
                            "size" to 153209,
                            "version" to "7.76.1"
                        ),
                        mapOf(
                            "arch" to "aarch64",
                            "build_id" to 2379863,
                            "buildroot_id" to 9721274,
                            "buildtime" to 1676560726,
                            "epoch" to null,
                            "external_repo_id" to 0,
                            "external_repo_name" to "INTERNAL",
                            "extra" to null,
                            "id" to 11958644,
                            "metadata_only" to false,
                            "name" to "libcurl-minimal",
                            "nvr" to "libcurl-minimal-7.76.1-23.el9",
                            "payloadhash" to "226428d3e656bf6d8e93c640b0cbe4ed",
                            "release" to "23.el9",
                            "size" to 226644,
                            "version" to "7.76.1"
                        ),
                    )
                )
            )
        )
        assertEquals(expected, response, "Deserialized array-based MethodResponse does not match expected value")
    }
}