package rules

import gamePlay.Move

enum class BoatPosition {
    ORIGINAL_RIVERSIDE, TARGET_RIVERSIDE
}

enum class RiverCrosserPosition {
    ORIGINAL_RIVERSIDE, BOAT, TARGET_RIVERSIDE
}

fun RiverCrosserPosition.newPosition(move: Move): RiverCrosserPosition {
    // TODO
    return RiverCrosserPosition.BOAT
}