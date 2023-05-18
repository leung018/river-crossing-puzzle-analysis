package game

import game.rules.*
import game.rules.classic.ClassicGameRules

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
                validateCanMoveBeApplied(i, move.type)
                newCrossers[i] =
                    newCrossers[i].copy(position = gamePlayPositions.crossers[i].position.newCrosserPosition(move.type))
            } catch (e: IndexOutOfBoundsException) {
                throw IllegalArgumentException("Target indices for the move don't exist in the original list")
            }
        }

        if (!isCrosserIndicesAtSamePosition(move.crosserIndices)) {
            throw IllegalArgumentException("Crossers at target indices must have the same position")
        }

        return this.copy(
            gamePlayPositions = GamePlayPositions(crossers = newCrossers, boatPosition = newBoatPosition(move.type)),
            pastMoves = pastMoves + listOf(move),
            totalCost = totalCost + moveTypeCostRules.getMoveCost(move.type)
        )
    }

    private fun isCrosserIndicesAtSamePosition(crosserIndices: Set<Int>): Boolean {
        return crosserIndices.map { gamePlayPositions.crossers[it].position }.distinct().let {
            it.size == 1
        }
    }

    private fun validateCanMoveBeApplied(crosserIndex: Int, moveType: MoveType) {
        val oldCrosserPosition = gamePlayPositions.crossers[crosserIndex].position

        if (oldCrosserPosition != RiverCrosserPosition.BOAT && moveType == MoveType.DRIVE_BOAT) {
            throw IllegalArgumentException("Crosser at index $crosserIndex is not at boat")
        }

        if (moveType == MoveType.TRANSIT) {
            if (oldCrosserPosition == RiverCrosserPosition.ORIGINAL_RIVERSIDE && gamePlayPositions.boatPosition == BoatPosition.TARGET_RIVERSIDE) {
                throw IllegalArgumentException("Crosser at index $crosserIndex is at original riverside and boat is at target riverside")
            }
            if (oldCrosserPosition == RiverCrosserPosition.TARGET_RIVERSIDE && gamePlayPositions.boatPosition == BoatPosition.ORIGINAL_RIVERSIDE) {
                throw IllegalArgumentException("Crosser at index $crosserIndex is at target riverside and boat is at original riverside")
            }
        }
    }

    private fun newBoatPosition(moveType: MoveType): BoatPosition {
        return when (moveType) {
            MoveType.DRIVE_BOAT -> gamePlayPositions.boatPosition.opposite()
            else -> gamePlayPositions.boatPosition
        }
    }

    private fun RiverCrosserPosition.newCrosserPosition(moveType: MoveType): RiverCrosserPosition {
        return when (moveType) {
            game.rules.MoveType.DRIVE_BOAT -> RiverCrosserPosition.BOAT
            game.rules.MoveType.TRANSIT -> when (this) {
                RiverCrosserPosition.ORIGINAL_RIVERSIDE, RiverCrosserPosition.TARGET_RIVERSIDE -> RiverCrosserPosition.BOAT
                RiverCrosserPosition.BOAT -> gamePlayPositions.boatPosition.nearbyRiversideForCrosser()
            }
        }
    }
}