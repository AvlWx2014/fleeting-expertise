package data.net.xmlrpc

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

@Serializable
@SerialName("methodResponse")
data class MethodResponse(
    @XmlElement(true)
    val value: MethodResponseType
)

@Serializable
sealed class MethodResponseType

@Serializable
@SerialName("fault")
data class Failure(
    @XmlElement(true)
    val value: Value
) : MethodResponseType()

@Serializable
@SerialName("params")
data class Success(
    @XmlElement(true)
    val data: Parameter
) : MethodResponseType()