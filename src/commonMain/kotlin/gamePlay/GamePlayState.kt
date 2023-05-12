package gamePlay

import rules.*
import rules.classic.ClassicGameRules

data class GamePlayPositions(
    val crossers: List<RiverCrosser>,
    val boatPosition: BoatPosition = BoatPosition.ORIGINAL_RIVERSIDE
)

data class GamePlayState(
    val gamePlayPositions: GamePlayPositions,
    val pastMoves: List<Pair<CrosserIndices, Move>> = listOf(),
    val totalCost: Int = 0,
) {

    /**
     * @throws IllegalArgumentException if any of the target indices not exist in the crossers list or the move is
     * not valid in current game play positions.
     */
    fun newStateAppliedMove(
        crosserIndicesAndMove: Pair<CrosserIndices, Move>,
        moveTypeCostRules: MoveTypeCostRules = ClassicGameRules
    ): GamePlayState {
        val newCrossers = gamePlayPositions.crossers.toMutableList()
        val (indices, move) = crosserIndicesAndMove

        for (i in indices) {
            try {
                val oldCrosserPosition = newCrossers[i].position

                // validate if the move is valid in current game play positions
                if (oldCrosserPosition != RiverCrosserPosition.BOAT && move == Move.DRIVE_BOAT) {
                    throw IllegalArgumentException("Crosser at index $i is not at boat")
                }
                if (move == Move.TRANSIT) {
                    if (oldCrosserPosition == RiverCrosserPosition.ORIGINAL_RIVERSIDE && gamePlayPositions.boatPosition == BoatPosition.TARGET_RIVERSIDE) {
                        throw IllegalArgumentException("Crosser at index $i is at original riverside and boat is at target riverside")
                    }
                    if (oldCrosserPosition == RiverCrosserPosition.TARGET_RIVERSIDE && gamePlayPositions.boatPosition == BoatPosition.ORIGINAL_RIVERSIDE) {
                        throw IllegalArgumentException("Crosser at index $i is at target riverside and boat is at original riverside")
                    }
                }

                newCrossers[i] = newCrossers[i].copy(position = oldCrosserPosition.newCrosserPosition(move))
            } catch (e: IndexOutOfBoundsException) {
                throw IllegalArgumentException("Target indices for the move don't exist in the original list")
            }
        }

        return this.copy(
            gamePlayPositions = GamePlayPositions(crossers = newCrossers, boatPosition = newBoatPosition(move)),
            pastMoves = pastMoves + listOf(crosserIndicesAndMove),
            totalCost = totalCost + moveTypeCostRules.getMoveCost(move)
        )
    }

    private fun newBoatPosition(move: Move): BoatPosition {
        return when (move) {
            Move.DRIVE_BOAT -> gamePlayPositions.boatPosition.opposite()
            else -> gamePlayPositions.boatPosition
        }
    }

    private fun RiverCrosserPosition.newCrosserPosition(move: Move): RiverCrosserPosition {
        return when (move) {
            Move.DRIVE_BOAT -> RiverCrosserPosition.BOAT
            Move.TRANSIT -> when (this) {
                RiverCrosserPosition.ORIGINAL_RIVERSIDE, RiverCrosserPosition.TARGET_RIVERSIDE -> RiverCrosserPosition.BOAT
                RiverCrosserPosition.BOAT -> gamePlayPositions.boatPosition.nearbyRiversideForCrosser()
            }
        }
    }
}