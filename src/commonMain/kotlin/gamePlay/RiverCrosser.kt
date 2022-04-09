package gamePlay

import rules.RiverCrosserPosition
import rules.RiverCrosserType

data class RiverCrosser(
    val type: RiverCrosserType,
    val position: RiverCrosserPosition = RiverCrosserPosition.ORIGINAL_RIVER_SIDE
)

