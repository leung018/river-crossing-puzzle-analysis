package gamePlay

import rules.BoatPosition
import rules.MoveType
import rules.MoveTypeCostRules
import rules.newPosition

data class CurrentPositions(
    val crossers: List<RiverCrosser>,
    val boatPosition: BoatPosition = BoatPosition.ORIGINAL_RIVERSIDE
)

data class GamePlayState(
    val currentPositions: CurrentPositions,
    val pastMoves: List<Pair<CrosserIndices, Move>>,
    val totalCost: Int = 0,
) {
    fun newStateAppliedMoves(
        crosserIndicesAndMove: Pair<CrosserIndices, Move>,
        moveTypeCostRules: MoveTypeCostRules
    ): GamePlayState {
        val newCrossers = currentPositions.crossers.toMutableList()
        val (indices, move) = crosserIndicesAndMove

        for (i in indices) {
            try {
                val oldPosition = newCrossers[i].position
                newCrossers[i] = newCrossers[i].copy(position = oldPosition.newPosition(move))
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
}