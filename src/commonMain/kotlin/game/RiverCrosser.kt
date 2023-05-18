package game

import game.rules.RiverCrosserPosition
import game.rules.RiverCrosserType

data class RiverCrosser(
    val type: RiverCrosserType,
    val position: RiverCrosserPosition = RiverCrosserPosition.ORIGINAL_RIVERSIDE
)

