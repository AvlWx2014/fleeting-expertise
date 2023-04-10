package model

enum class BuildState {
    BUILDING,
    COMPLETE,
    DELETED,
    FAILED,
    CANCELED;

    companion object {
        // only allocate the array of values one time and use that always
        private val values: Array<BuildState> = values()

        operator fun get(ord: Int): BuildState = values[ord]
    }
}
