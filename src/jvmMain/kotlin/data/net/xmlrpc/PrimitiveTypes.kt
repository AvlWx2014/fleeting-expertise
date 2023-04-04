@file:JvmName("PrimitiveTypeExtensions")

package data.net.xmlrpc

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface PrimitiveType : ValueType

@JvmInline
@Serializable
@SerialName("string")
value class StringType(val value: String) : PrimitiveType

@JvmInline
@Serializable
@SerialName("int")
value class IntType(val value: Int) : PrimitiveType

@JvmInline
@Serializable
@SerialName("boolean")
value class BooleanType private constructor(
    val value: Int
) : PrimitiveType {

    fun toBoolean(): Boolean = when (value) {
        0 -> false
        1 -> true
        else -> error("BooleanType value $value is invalid.")
    }

    companion object {
        operator fun invoke(value: Boolean) = BooleanType(if (value) 1 else 0)
    }
}

@JvmInline
@Serializable
@SerialName("double")
value class DoubleType(val value: Double) : PrimitiveType

@Serializable
@SerialName("nil")
object NullType : PrimitiveType {
    override fun toString(): String = "nil"
}
