package gamePlay

import rules.BoatPosition
import rules.GameSituationRules
import rules.Move
import rules.RiverCrosserPosition

class GameSituationTeller(private val gamePlayPositions: GamePlayPositions, val rules: GameSituationRules) {
    init {
        validateCrossers()
    }

    constructor(
        crossers: List<RiverCrosser>, boatPosition: BoatPosition, rules: GameSituationRules
    ) : this(GamePlayPositions(crossers, boatPosition), rules)

    private fun validateCrossers() {
        if (gamePlayPositions.crossers.isEmpty()) {
            throw IllegalArgumentException("Input crossers cannot be empty")
        }
        validateCrossersType()
        validateCrossersPosition()
    }

    private fun validateCrossersType() {
        for (crosser in gamePlayPositions.crossers) {
            if (!rules.validRiverCrosserTypes.contains(crosser.type)) {
                throw IllegalArgumentException("Crossers contain type that rules not exist")
            }
        }
    }

    private fun validateCrossersPosition() {
        val inBoatCount = gamePlayPositions.crossers.count { it.position == RiverCrosserPosition.BOAT }
        if (inBoatCount > rules.boatCapacity) {
            throw IllegalArgumentException("More crossers are in the boat than its capacity")
        }
    }

    fun getCurrentValidMoves(): Set<Pair<CrosserIndices, Move>> {
        val newMoves = mutableSetOf<Pair<CrosserIndices, Move>>()
        for ((index, crosser) in gamePlayPositions.crossers.withIndex()) {
            if (crosser.position == RiverCrosserPosition.ORIGINAL_RIVERSIDE) {
                newMoves.add(setOf(index) to Move.TRANSIT)
            } else if (crosser.position == RiverCrosserPosition.BOAT) {
                newMoves.add(
                    getCrossersIndicesOfPosition(RiverCrosserPosition.BOAT) to Move.DRIVE_BOAT
                )
                newMoves.add(setOf(index) to Move.TRANSIT)
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

    fun isWin(): Boolean {
        TODO()
    }

    fun isGameOver(): Boolean {
        TODO()
    }
}