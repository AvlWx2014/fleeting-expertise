package data.net.xmlrpc

import nl.adaptivity.xmlutil.core.impl.multiplatform.name
import kotlin.contracts.contract

internal fun checkStruct(value: ValueType) {
    contract {
        returns() implies (value is StructType)
    }

    check(value is StructType) {
        "Could not use value of type ${value::class.name}. Expected ${StructType::class.name}."
    }
}

internal fun checkArray(value: ValueType) {
    contract {
        returns() implies (value is ArrayType)
    }

    check(value is ArrayType) {
        "Could not use value of type ${value::class.name}. Expected ${ArrayType::class.name}."
    }
}