package gamePlay

import rules.GameSituationRules
import Move
import RiverCrosser

class GameSituationTeller(val crossers: List<RiverCrosser>, val rules: GameSituationRules) {
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