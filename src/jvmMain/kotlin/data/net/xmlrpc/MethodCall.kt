package data.net.xmlrpc

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlChildrenName
import nl.adaptivity.xmlutil.serialization.XmlElement

@Serializable
@SerialName("methodCall")
data class MethodCall(
    @XmlElement(true)
    val methodName: String,

    @SerialName("params")
    @XmlChildrenName("param", "", "")
    val parameters: List<Parameter>
)