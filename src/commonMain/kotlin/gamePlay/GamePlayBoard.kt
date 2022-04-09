package gamePlay

import rules.GameRules

typealias CrosserIndices = Set<Int>

class GamePlayBoard private constructor(crossers: List<RiverCrosser>, private val rules: GameRules) {
    companion object {
        fun getLowestCostGameSolvingPossibleMoves(
            initialCrossers: List<RiverCrosser>,
            rules: GameRules
        ): Set<List<Pair<CrosserIndices, Move>>> {
            return GamePlayBoard(initialCrossers, rules).getLowestCostGameSolvingPossibleMoves()
        }

    }

    private var transitedCrossersSet: Set<List<RiverCrosser>> = mutableSetOf()

    private val activeGamePlayStatesList =
        MutableList(1) { GamePlayState(crossers, emptyList()) }
    private val winGamePlayStatesWithLowestTotalCostList =
        mutableListOf<GamePlayState>() //all totalCost are same in this list

    private fun getLowestCostGameSolvingPossibleMoves(): Set<List<Pair<CrosserIndices, Move>>> {
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
