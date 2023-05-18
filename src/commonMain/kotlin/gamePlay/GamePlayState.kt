package gamePlay

import rules.*
import rules.classic.ClassicGameRules

/**
 * @param crosserIndices indices of crossers to move. They must have the same position.
 * @param type type of move to apply.
 */
data class Move(
    val crosserIndices: Set<Int>,
    val type: MoveType
)

data class GamePlayPositions(
    val crossers: List<RiverCrosser>,
    val boatPosition: BoatPosition = BoatPosition.ORIGINAL_RIVERSIDE
) {
    /**
     * @return positions of initial crossers at the start of the game.
     */
    constructor(initialCrosserTypes: List<RiverCrosserType>) : this(
        crossers = initialCrosserTypes.map { RiverCrosser(it) }
    )
}

data class GamePlayState(
    val gamePlayPositions: GamePlayPositions,
    val pastMoves: List<Move> = listOf(),
    val totalCost: Int = 0,
) {

    /**
     * @throws IllegalArgumentException if any of the target indices not exist in the crossers list or the move is
     * not valid in current game play positions.
     */
    fun newStateAppliedMove(
        move: Move,
        moveTypeCostRules: MoveTypeCostRules = ClassicGameRules
    ): GamePlayState {
        val newCrossers = gamePlayPositions.crossers.toMutableList()

        for (i in move.crosserIndices) {
            try {
                val oldCrosserPosition = newCrossers[i].position

                // validate if the move is valid in current game play positions
                if (oldCrosserPosition != RiverCrosserPosition.BOAT && move.type == MoveType.DRIVE_BOAT) {
                    throw IllegalArgumentException("Crosser at index $i is not at boat")
                }
                if (move.type == MoveType.TRANSIT) {
                    if (oldCrosserPosition == RiverCrosserPosition.ORIGINAL_RIVERSIDE && gamePlayPositions.boatPosition == BoatPosition.TARGET_RIVERSIDE) {
                        throw IllegalArgumentException("Crosser at index $i is at original riverside and boat is at target riverside")
                    }
                    if (oldCrosserPosition == RiverCrosserPosition.TARGET_RIVERSIDE && gamePlayPositions.boatPosition == BoatPosition.ORIGINAL_RIVERSIDE) {
                        throw IllegalArgumentException("Crosser at index $i is at target riverside and boat is at original riverside")
                    }
                }

                newCrossers[i] = newCrossers[i].copy(position = oldCrosserPosition.newCrosserPosition(move.type))
            } catch (e: IndexOutOfBoundsException) {
                throw IllegalArgumentException("Target indices for the move don't exist in the original list")
            }
        }

        move.crosserIndices.map { gamePlayPositions.crossers[it] }.distinct().let {
            if (it.size != 1) {
                throw IllegalArgumentException("Crossers at target indices must have the same position")
            }
        }

        return this.copy(
            gamePlayPositions = GamePlayPositions(crossers = newCrossers, boatPosition = newBoatPosition(move.type)),
            pastMoves = pastMoves + listOf(move),
            totalCost = totalCost + moveTypeCostRules.getMoveCost(move.type)
        )
    }

    private fun newBoatPosition(moveType: MoveType): BoatPosition {
        return when (moveType) {
            MoveType.DRIVE_BOAT -> gamePlayPositions.boatPosition.opposite()
            else -> gamePlayPositions.boatPosition
        }
    }

    private fun RiverCrosserPosition.newCrosserPosition(moveType: MoveType): RiverCrosserPosition {
        return when (moveType) {
            MoveType.DRIVE_BOAT -> RiverCrosserPosition.BOAT
            MoveType.TRANSIT -> when (this) {
                RiverCrosserPosition.ORIGINAL_RIVERSIDE, RiverCrosserPosition.TARGET_RIVERSIDE -> RiverCrosserPosition.BOAT
                RiverCrosserPosition.BOAT -> gamePlayPositions.boatPosition.nearbyRiversideForCrosser()
            }
        }
    }
}