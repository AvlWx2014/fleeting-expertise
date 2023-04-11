package model

data class BuildrootRpm(
    val id: Int,
    val name: String,
    val version: String,
    val release: String,
    val arch: String
)