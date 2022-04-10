package gamePlay

import rules.GameSituationRules

class GameSituationTeller(val crossers: List<RiverCrosser>, val rules: GameSituationRules) {
    init {
        validateCrossers()
    }

    private fun validateCrossers() {
        if (crossers.isEmpty()) {
            throw IllegalArgumentException("Input crossers cannot be empty")
        }
        validateCrossersType()
    }

    private fun validateCrossersType() {
        for (crosser in crossers) {
            if (!rules.validRiverCrosserTypes.contains(crosser.type)) {
                throw IllegalArgumentException("Crossers contain type that rules not exist")
            }
        }
    }

    fun getCurrentValidMoves(): Pair<CrosserIndices, Move> {
        TODO()
    }

    fun isWin(): Boolean {
        TODO()
    }

    fun isGameOver(): Boolean {
        TODO()
    }
}