package gamePlay

import rules.MoveTypeCostRules

data class GamePlayState(
    val crossers: List<RiverCrosser>,
    val pastMoves: List<Pair<CrosserIndices, Move>>,
    val totalCost: Int = 0,
) {
    fun newStateAppliedMoves(
        crosserIndicesAndMove: Pair<CrosserIndices, Move>,
        moveTypeCostRules: MoveTypeCostRules
    ): GamePlayState {
        val newCrossers = crossers.toMutableList()
        val (indices, move) = crosserIndicesAndMove

        for (i in indices) {
            try {
                newCrossers[i] = newCrossers[i].copy(position = move.targetPosition)
            } catch (e: IndexOutOfBoundsException) {
                throw IllegalArgumentException("Target indices for the move not exist in the original list")
            }
        }

        return this.copy(
            crossers = newCrossers,
            pastMoves = pastMoves + listOf(crosserIndicesAndMove),
            totalCost = totalCost + moveTypeCostRules.getMoveCost(move.moveType)
        )
    }
}