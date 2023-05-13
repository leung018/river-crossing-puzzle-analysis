package gamePlay

import rules.GameRules
import rules.Move
import rules.RiverCrosserType

typealias CrosserIndices = Set<Int>

class GamePlayBoard private constructor(gamePlayPositions: GamePlayPositions, private val rules: GameRules) {

    companion object {
        /**
         * @return a set of list of pairs of crosser indices and moves, each list is one of the lowest cost solutions of the game.
         * If there are many lowest cost solution list, only those with minimum length will be returned.
         * Each pair means the indices of crossers to move and the move to apply.
         */
        fun getMinCostGameSolvingMoves(
            crosserTypes: List<RiverCrosserType>,
            rules: GameRules
        ): Set<List<Pair<CrosserIndices, Move>>> {
            TODO()
        }

    }

    private var transitedPositions: Set<GamePlayPositions> = mutableSetOf()

    private val activeGamePlayStates = mutableSetOf(GamePlayState(gamePlayPositions))

    private val minCostWinningStates = mutableListOf<GamePlayState>()

    private fun getMinCostGameSolvingMoves(): Set<List<Pair<CrosserIndices, Move>>> {
        TODO()
        /* For each state in activeGamePlayStates:
                obtain each valid moves (GameSituationTeller.getCurrentValidMoves)
                futureStates = []
                For each move:
                    get new state after applied move (GamePlayState.newStateAppliedMove)
                    if the new state's position not in transitedPositions
                        add to transitedPositions
                        if this new state is win (GameSituationTeller.isWin):
                            if totalCost is equal to those in minCostWinningStates 
                                add to minCostWinningStates
                            if totalCost is less than those in minCostWinningStates
                                drop the minCostWinningStates and let it contain this new state only
                        else:
                            if totalCost is equal to those in minCostWinningStates
                                continue;
                            if this new state is not game over (GameSituationTeller.isGameOver)
                                add to futureStates
                replace old state by futureStates in activeGamePlayStates
           Repeat above until activeGamePlayStates is Empty

           return past moves of minCostWinningStates
        */

    }
}
