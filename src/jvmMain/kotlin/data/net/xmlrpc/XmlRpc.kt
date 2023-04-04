package data.net.xmlrpc

import kotlinx.serialization.modules.SerializersModule
import nl.adaptivity.xmlutil.XmlDeclMode
import nl.adaptivity.xmlutil.serialization.XML


val XmlModule = SerializersModule {}

val XmlFormat = XML(XmlModule) {
    autoPolymorphic = true
    xmlDeclMode = XmlDeclMode.Auto
    indentString = "  "
}

fun methodCall(methodName: String, vararg params: Any): MethodCall {
    return MethodCall(
        methodName,
        params.map(::parameterOf)
    )
}