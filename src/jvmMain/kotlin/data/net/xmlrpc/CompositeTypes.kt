@file:JvmName("CompositeTypeExtensions")

package data.net.xmlrpc

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nl.adaptivity.xmlutil.core.impl.multiplatform.name
import nl.adaptivity.xmlutil.serialization.XmlChildrenName
import nl.adaptivity.xmlutil.serialization.XmlElement
import java.lang.IllegalStateException

@Serializable
sealed interface CompositeType : ValueType

@Serializable
@SerialName("array")
data class ArrayType(
    @XmlElement(true)
    @XmlChildrenName("value", "", "")
    val data: List<Value>
) : CompositeType, List<Value> by data

@Serializable
@SerialName("struct")
data class StructType(private val members: List<Member>) : CompositeType {
    @Transient
    val properties: Map<String, ValueType> = members.associate {
        it.name to it.value.value
    }

    internal operator fun get(key: String): ValueType {
        require(key in properties) {
            "StructType has no property named $key"
        }
        return properties[key] ?: error("Property $key was mapped to a null value.")
    }
}

fun StructType.int(key: String): Int = when (val value = this[key]) {
    is IntType -> value.value
    else -> error("Expected property $key to be an IntType but was ${value::class.name}")
}

// TODO: doesn't handle NullType very cleanly (implicitly if non-nullable version throws)
fun StructType.intOrNull(key: String): Int? = try {
    int(key)
} catch (ise: IllegalStateException) {
    null
}

fun StructType.double(key: String): Double = when (val value = this[key]) {
    is DoubleType -> value.value
    else -> error("Expected property $key to be an DoubleType but was ${value::class.name}")
}

// TODO: doesn't handle NullType very cleanly (implicitly if non-nullable version throws)
fun StructType.doubleOrNull(key: String): Double? = try {
    double(key)
} catch (ise: IllegalStateException) {
    null
}

fun StructType.string(key: String): String = when (val value = this[key]) {
    is StringType -> value.value
    else -> error("Expected property $key to be an StringType but was ${value::class.name}")
}

// TODO: doesn't handle NullType very cleanly (implicitly if non-nullable version throws)
fun StructType.stringOrNull(key: String): String? = try {
    string(key)
} catch (ise: IllegalStateException) {
    null
}

fun StructType.boolean(key: String): Boolean = when (val value = this[key]) {
    is BooleanType -> value.toBoolean()
    else -> error("Expected property $key to be an BooleanType but was ${value::class.name}")
}

// TODO: doesn't handle NullType very cleanly (implicitly if non-nullable version throws)
fun StructType.booleanOrNull(key: String): Boolean? = try {
    boolean(key)
} catch (ise: IllegalStateException) {
    null
}
