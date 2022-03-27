const val BOAT_SIZE = 2

typealias CrosserIndices = Set<Int>

data class GamingState(
    val crossers: List<RiverCrosser>,
    val pastMoves: List<Pair<CrosserIndices, Move>>,
    val totalCost: Int = 0,
) {
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
    private var transitedCrossersSet: Set<List<RiverCrosser>> = setOf()

    private val activeGamingStatesList =
        MutableList(1) { GamingState(crossers, emptyList()) }
    private val winGamingStatesWithLowestTotalCostList =
        mutableListOf<GamingState>() //all totalCost are same in this list

    fun getLowestCostGameSolvingPossibleMovesList(): List<Pair<CrosserIndices, Move>> {
        TODO()
        /* For each state () in activeGamingStatesList:
                obtain each valid moves (GameSituationTeller.getCurrentValidMoves)
                futureStates = []
                For each move:
                    get new state after applied move (GamingSate.newStateAppliedMoves)
                    if the new state's crossers not in transitedCrossersSet
                        add to transitedCrossersSet
                        if this new state is win (GameSituationTeller.isWin):
                            if totalCost is equal to those in winGamingStatesWithLowestTotalCostList
                                add to list
                                skip this loop;
                            if totalCost is less than those
                                drop the winGameStatesList and let the list contain this new state only
                            skip this loop;
                        if totalCost is equal to those in winGamingStatesWithLowestTotalCostList
                            skip this loop;
                        if this new state is not game over (GameSituationTeller.isGameOver)
                            add to futureStates
                replace old state by futureStates in activeGamingStatesList
           Repeat above until activeGamingStatesList is Empty
        */
    }
}
