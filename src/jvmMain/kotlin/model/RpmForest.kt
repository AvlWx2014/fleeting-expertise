package model

data class RpmForest(
    val trees: Collection<RpmTree>
) : Collection<RpmTree> by trees

fun RpmForest(vararg trees: RpmTree): RpmForest = RpmForest(trees.toList())
