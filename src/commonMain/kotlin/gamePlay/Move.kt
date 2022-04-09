package gamePlay

import rules.MoveType
import rules.RiverCrosserPosition

data class Move(val moveType: MoveType, val targetPosition: RiverCrosserPosition)

