const val BOAT_SIZE = 2

typealias CrosserIndices = Set<Int>

data class GamingState(val crossers: List<RiverCrosser>, val totalCost: Int = 0) {
    fun newStateAppliedMoves(crosserIndicesAndMove: Pair<CrosserIndices, Move>): GamingState {
        TODO()
    }
}

class GameSituationTeller(private val crossers: List<RiverCrosser>) {
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

class GamingBoard(crossers: List<RiverCrosser>) {
    private val gamingStatesList = MutableList(1) { GamingState(crossers) }

    fun getLowestCostGameSolvingPossibleMovesList(): Pair<CrosserIndices, Move> {
        TODO()
    }
}
