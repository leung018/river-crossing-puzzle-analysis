data class RiverCrosser(val type: RiverCrosserType) {
    val position = Position.ORIGINAL_RIVER_SIDE
}

enum class RiverCrosserType {
    DOG, FATHER, MOTHER, SON, DAUGHTER
}

enum class Position {
    ORIGINAL_RIVER_SIDE, BOAT, TARGET_RIVER_SIDE
}

val CAN_DRIVE_BOAT = setOf(RiverCrosserType.FATHER, RiverCrosserType.MOTHER)

fun canGameContinue(crossersInSamePosition: Set<RiverCrosser>): Boolean {
    TODO()
}
