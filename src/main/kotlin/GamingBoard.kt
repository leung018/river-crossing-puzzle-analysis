const val BOAT_SIZE = 2

typealias CrosserIndexAndMove = Pair<Int, Move>

class GamingState(val crossers: List<RiverCrosser>) {
    var totalCost = 0
    fun getCurrentValidMoves(): Set<CrosserIndexAndMove> {
        TODO()
    }

    fun newStateAppliedMoves(movesList: Set<CrosserIndexAndMove>): GamingState {
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

    fun getShortestGameSolvingMovesList(): List<Set<CrosserIndexAndMove>> {
        TODO()
    }
}

abstract class Move {
    abstract val cost: Int
    abstract fun move(crosser: RiverCrosser): RiverCrosser
    abstract val description: String
}