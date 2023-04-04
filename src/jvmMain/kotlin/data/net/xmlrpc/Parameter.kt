package data.net.xmlrpc

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

@Serializable
@SerialName("param")
data class Parameter(
    @XmlElement(true)
    val value: Value
)

fun parameterOf(it: Any?): Parameter = Parameter(valueOf(it))