const val BOAT_SIZE = 2

typealias CrosserIndices = Set<Int>

data class GamePlayState(
    val crossers: List<RiverCrosser>,
    val pastMoves: List<Pair<CrosserIndices, Move>>,
    val totalCost: Int = 0,
) {
    fun newStateAppliedMoves(crosserIndicesAndMove: Pair<CrosserIndices, Move>): GamePlayState {
        TODO()
    }
}

class GameSituationTeller(private val crossers: List<RiverCrosser>, rules: GameRules) {
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

class GamePlayBoard private constructor(crossers: List<RiverCrosser>, private val rules: GameRules) {
    companion object {
        fun getLowestCostGameSolvingPossibleMovesList(
            crossers: List<RiverCrosser>,
            rules: GameRules
        ): List<Pair<CrosserIndices, Move>> {
            return GamePlayBoard(crossers, rules).getLowestCostGameSolvingPossibleMovesList()
        }

    }

    private var transitedCrossersSet: Set<List<RiverCrosser>> = mutableSetOf()

    private val activeGamePlayStatesList =
        MutableList(1) { GamePlayState(crossers, emptyList()) }
    private val winGamePlayStatesWithLowestTotalCostList =
        mutableListOf<GamePlayState>() //all totalCost are same in this list

    private fun getLowestCostGameSolvingPossibleMovesList(): List<Pair<CrosserIndices, Move>> {
        TODO()
        /* For each state () in activeGamePlayStatesList:
                obtain each valid moves (GameSituationTeller.getCurrentValidMoves)
                futureStates = []
                For each move:
                    get new state after applied move (GamePlayState.newStateAppliedMoves)
                    if the new state's crossers not in transitedCrossersSet
                        add to transitedCrossersSet
                        if this new state is win (GameSituationTeller.isWin):
                            if totalCost is equal to those in winGamePlayStatesWithLowestTotalCostList
                                add to list
                                skip this loop;
                            if totalCost is less than those
                                drop the winGameStatesList and let the list contain this new state only
                            skip this loop;
                        if totalCost is equal to those in winGamePlayStatesWithLowestTotalCostList
                            skip this loop;
                        if this new state is not game over (GameSituationTeller.isGameOver)
                            add to futureStates
                replace old state by futureStates in activeGamePlayStatesList
           Repeat above until activeGamePlayStatesList is Empty
        */
    }
}
