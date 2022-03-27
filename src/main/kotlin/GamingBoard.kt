const val BOAT_SIZE = 2

typealias CrosserIndices = Set<Int>

class GamingState(val crossers: List<RiverCrosser>) {
    var totalCost = 0
    fun getCurrentValidMoves(): Pair<CrosserIndices, Move> {
        TODO()
    }

    fun newStateAppliedMoves(movesList: Pair<CrosserIndices, Move>): GamingState {
        TODO()
    }

    fun isWin(): Boolean {
        TODO()
    }

    fun isGameOver(): Boolean {
        return canGameContinue(crossers.toSet())
    }
}

class GamingBoard(crossers: List<RiverCrosser>) {
    private val gamingStatesList = MutableList(1) { GamingState(crossers) }

    fun getLowestCostGameSolvingPossibleMovesList(): Pair<CrosserIndices, Move> {
        TODO()
    }
}
