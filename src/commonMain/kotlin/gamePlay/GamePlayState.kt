package gamePlay

import rules.BoatPosition
import rules.MoveType
import rules.MoveTypeCostRules
import rules.RiverCrosserPosition
import rules.classic.ClassicGameRules

data class CurrentPositions(
    val crossers: List<RiverCrosser>,
    val boatPosition: BoatPosition = BoatPosition.ORIGINAL_RIVERSIDE
)

data class GamePlayState(
    val currentPositions: CurrentPositions,
    val pastMoves: List<Pair<CrosserIndices, Move>> = listOf(),
    val totalCost: Int = 0,
) {

    /**
     * @throws IllegalArgumentException if the target indices for the move not exist in the original list or the move is not valid
     */
    fun newStateAppliedMoves(
        crosserIndicesAndMove: Pair<CrosserIndices, Move>,
        moveTypeCostRules: MoveTypeCostRules = ClassicGameRules
    ): GamePlayState {
        val newCrossers = currentPositions.crossers.toMutableList()
        val (indices, move) = crosserIndicesAndMove

        for (i in indices) {
            try {
                val oldCrosserPosition = newCrossers[i].position
                newCrossers[i] = newCrossers[i].copy(position = oldCrosserPosition.newCrosserPosition(move))
            } catch (e: IndexOutOfBoundsException) {
                throw IllegalArgumentException("Target indices for the move not exist in the original list")
            }
        }

        return this.copy(
            currentPositions = CurrentPositions(crossers = newCrossers, boatPosition = newBoatPosition(move)),
            pastMoves = pastMoves + listOf(crosserIndicesAndMove),
            totalCost = totalCost + moveTypeCostRules.getMoveCost(move.moveType)
        )
    }

    private fun newBoatPosition(move: Move): BoatPosition {
        return when (move.moveType) {
            MoveType.DRIVE_BOAT -> {
                when (currentPositions.boatPosition) {
                    BoatPosition.ORIGINAL_RIVERSIDE -> BoatPosition.TARGET_RIVERSIDE
                    BoatPosition.TARGET_RIVERSIDE -> BoatPosition.ORIGINAL_RIVERSIDE
                }
            }

            else -> currentPositions.boatPosition
        }
    }

    private fun RiverCrosserPosition.newCrosserPosition(move: Move): RiverCrosserPosition {
        return when (move.moveType) {
            MoveType.DRIVE_BOAT -> RiverCrosserPosition.BOAT
            MoveType.TRANSIT -> when (this) {
                RiverCrosserPosition.ORIGINAL_RIVERSIDE, RiverCrosserPosition.TARGET_RIVERSIDE -> RiverCrosserPosition.BOAT
                RiverCrosserPosition.BOAT -> when (currentPositions.boatPosition) {
                    BoatPosition.ORIGINAL_RIVERSIDE -> RiverCrosserPosition.ORIGINAL_RIVERSIDE
                    BoatPosition.TARGET_RIVERSIDE -> RiverCrosserPosition.TARGET_RIVERSIDE
                }
            }
        }
    }
}