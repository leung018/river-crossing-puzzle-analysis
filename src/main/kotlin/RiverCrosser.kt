data class RiverCrosser(val type: RiverCrosserType) {
    val position = RiverCrosserPosition.ORIGINAL_RIVER_SIDE
}

enum class RiverCrosserType {
    DOG, FATHER, MOTHER, SON, DAUGHTER
}

enum class RiverCrosserPosition {
    ORIGINAL_RIVER_SIDE, BOAT_ON_ORIGINAL_RIVER_SIZE, BOAT_ON_TARGET_RIVER_SIDE, TARGET_RIVER_SIDE
}

val CAN_DRIVE_BOAT = setOf(RiverCrosserType.FATHER, RiverCrosserType.MOTHER)

fun canGameContinue(crossersInSamePosition: Set<RiverCrosser>): Boolean {
    TODO()
}
