package data.net.xmlrpc

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

@Serializable
@SerialName("member")
data class Member(
    @XmlElement(true)
    val name: String,

    @XmlElement(true)
    val value: Value
)