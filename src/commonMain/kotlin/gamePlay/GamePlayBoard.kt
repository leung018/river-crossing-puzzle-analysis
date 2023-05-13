package gamePlay

import rules.GameRules
import rules.Move
import rules.RiverCrosserType

typealias CrosserIndices = Set<Int>

class GamePlayBoard private constructor(gamePlayPositions: GamePlayPositions, private val rules: GameRules) {

    companion object {
        /**
         * @param crosserTypes the types of crossers at the original riverside.
         *
         * @return a set of list of pairs of crosser indices and moves, each list is one of the lowest cost solutions of the game.
         * Each pair means the indices of crossers to move and the move to apply. The indices are corresponding to input crosserTypes.
         *
         * If there are many lowest cost solutions, only those with minimum length will be returned.
         */
        fun getMinCostGameSolvingMoves(
            crosserTypes: List<RiverCrosserType>,
            rules: GameRules
        ): Set<List<Pair<CrosserIndices, Move>>> {
            return GamePlayBoard(
                GamePlayPositions(crosserTypes.map { RiverCrosser(it) }),
                rules
            ).getMinCostGameSolvingMoves()
        }

    }

    private var transitedPositions: MutableSet<GamePlayPositions> = mutableSetOf()

    private val activeGamePlayStates = mutableSetOf(GamePlayState(gamePlayPositions))

    private val minCostWinningStates = mutableListOf<GamePlayState>()

    private fun getMinWinningCost(): Int {
        return if (minCostWinningStates.isEmpty()) {
            Int.MAX_VALUE
        } else {
            minCostWinningStates[0].totalCost
        }
    }

    private fun getMinCostGameSolvingMoves(): Set<List<Pair<CrosserIndices, Move>>> {
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

        while (activeGamePlayStates.isNotEmpty()) {
            for (currentState in activeGamePlayStates) {
                val nextStates = mutableListOf<GamePlayState>()
                val possibleMoves = newGameSituationTeller(currentState.gamePlayPositions).getCurrentValidMoves()

                for ((crosserIndices, move) in possibleMoves) {
                    val newState = currentState.newStateAppliedMove(crosserIndices to move, rules)
                    if (newState.gamePlayPositions !in transitedPositions) {
                        transitedPositions.add(newState.gamePlayPositions)
                        if (newGameSituationTeller(newState.gamePlayPositions).isWin()) {
                            if (newState.totalCost == getMinWinningCost()) {
                                minCostWinningStates.add(newState)
                            } else if (newState.totalCost < getMinWinningCost()) {
                                minCostWinningStates.clear()
                                minCostWinningStates.add(newState)
                            }
                        } else {
                            if (newState.totalCost == getMinWinningCost()) {
                                continue
                            } else if (!newGameSituationTeller(newState.gamePlayPositions).isGameOver()) {
                                nextStates.add(newState)
                            }
                        }
                    }
                }

                activeGamePlayStates.remove(currentState)
                activeGamePlayStates.addAll(nextStates)
            }
        }

        return minCostWinningStates.map { it.pastMoves }.toSet()
    }

    private fun newGameSituationTeller(positions: GamePlayPositions): GameSituationTeller {
        return GameSituationTeller(positions, rules)
    }
}
