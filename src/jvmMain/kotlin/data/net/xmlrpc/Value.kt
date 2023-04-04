package data.net.xmlrpc

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.core.impl.multiplatform.name
import nl.adaptivity.xmlutil.serialization.XmlElement

@Serializable
@SerialName("value")
data class Value(
    @XmlElement(value=true)
    val value: ValueType
)

fun valueOf(it: Any?): Value {
    val value = when (it) {
        is String -> StringType(it)
        is Int -> IntType(it)
        is Double -> DoubleType(it)
        is Boolean -> BooleanType(it)
        is Collection<*> -> ArrayType(it.map(::valueOf))
        is Map<*, *> -> {
            it.map { (k, v) -> Member(k.toString(), valueOf(v)) }.let(::StructType)
        }
        null -> NullType
        else -> error("${it::class.name} is not a supported XML-RPC value type.")
    }

    return Value(value)
}
