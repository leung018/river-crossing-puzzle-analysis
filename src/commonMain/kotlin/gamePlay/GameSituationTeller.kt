package gamePlay

import rules.GameSituationRules

class GameSituationTeller(val crossers: List<RiverCrosser>, val rules: GameSituationRules) {
    /* TODO: Use rules.validRiverCrosserTypes to check whether input is valid in constructor
        Throw exception if not valid
    */

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