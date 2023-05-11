package gamePlay

import rules.GameSituationRules
import rules.Move
import rules.RiverCrosserPosition
import rules.nearRiverCrosserPosition
import util.getCombinations

class GameSituationTeller(private val gamePlayPositions: GamePlayPositions, private val rules: GameSituationRules) {
    init {
        validateCrossers()
    }

    private fun validateCrossers() {
        if (gamePlayPositions.crossers.isEmpty()) {
            throw IllegalArgumentException("Input crossers cannot be empty")
        }
        validateCrosserTypes()
        validateCrosserPositions()
    }

    private fun validateCrosserTypes() {
        for (crosser in gamePlayPositions.crossers) {
            if (!rules.validRiverCrosserTypes.contains(crosser.type)) {
                throw IllegalArgumentException("Crossers contain type that does not exist in rules")
            }
        }
    }

    private fun validateCrosserPositions() {
        val inBoatCount = gamePlayPositions.crossers.count { it.position == RiverCrosserPosition.BOAT }
        if (inBoatCount > rules.boatCapacity) {
            throw IllegalArgumentException("More crossers are in the boat than its capacity")
        }
    }

    /**
     * @return set of pairs of crossers indices and move that can be applied to a valid game state.
     * But it doesn't mean that the game state after applying the move will not game over.
     */
    fun getCurrentValidMoves(): Set<Pair<CrosserIndices, Move>> {
        val newMoves = mutableSetOf<Pair<CrosserIndices, Move>>()

        // moves of crossers on riverside
        getCrossersIndicesOfPosition(gamePlayPositions.boatPosition.nearRiverCrosserPosition()).let {
            if (it.isNotEmpty()) {
                getCombinations(it, rules.boatCapacity).forEach { possibleIndices ->
                    newMoves.add(
                        possibleIndices to Move.TRANSIT
                    )
                }
            }
        }

        // moves of crossers on boat
        getCrossersIndicesOfPosition(RiverCrosserPosition.BOAT).let {
            if (it.isNotEmpty()) {
                // moves that drive boat
                if (canDriveBoat(it)) {
                    newMoves.add(
                        it to Move.DRIVE_BOAT
                    )
                }

                // moves that transit to riverside
                getCombinations(it, it.size).forEach { possibleIndices ->
                    newMoves.add(
                        possibleIndices to Move.TRANSIT
                    )
                }
            }
        }

        return newMoves
    }

    private fun getCrossersIndicesOfPosition(targetPosition: RiverCrosserPosition): CrosserIndices {
        val crosserIndices = mutableSetOf<Int>()
        for ((index, crosser) in gamePlayPositions.crossers.withIndex()) {
            if (crosser.position == targetPosition) {
                crosserIndices.add(index)
            }
        }
        return crosserIndices
    }

    private fun canDriveBoat(crosserIndices: CrosserIndices): Boolean {
        return crosserIndices.map { index -> gamePlayPositions.crossers[index] }
            .any { crosser -> rules.boatDriverTypes.contains(crosser.type) }
    }

    fun isWin(): Boolean {
        TODO()
    }

    fun isGameOver(): Boolean {
        TODO()
    }
}