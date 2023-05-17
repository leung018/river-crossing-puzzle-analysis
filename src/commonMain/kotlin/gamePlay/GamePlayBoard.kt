package gamePlay

import rules.GameRules
import rules.Move
import rules.RiverCrosserType

typealias CrosserIndices = Set<Int>

class GamePlayBoard private constructor(gamePlayPositions: GamePlayPositions, private val rules: GameRules) {

    companion object {
        /**
         * @param crosserTypes the types of crossers at the original riverside.
         */
        fun getMinCostGameSolvingMoves(
            crosserTypes: List<RiverCrosserType>,
            rules: GameRules
        ): List<Pair<CrosserIndices, Move>>? {
            return GamePlayBoard(
                GamePlayPositions(crosserTypes),
                rules
            ).getMinCostGameSolvingMoves()
        }

    }

    private val activeGamePlayStates = mutableSetOf(GamePlayState(gamePlayPositions))

    /**
     * The key of the map means transited positions that are neither game over nor game win.
     */
    private val activePositionsToMinCost: MutableMap<GamePlayPositions, Int> = mutableMapOf(
        gamePlayPositions to 0
    )

    /**
     *  optimalWinningState is the state that using the minimum sizes of moves to achieve the minimum cost of solutions.
     */
    private var optimalWinningState: GamePlayState? = null

    private fun getMinWinningCost(): Int {
        return optimalWinningState?.totalCost ?: Int.MAX_VALUE
    }

    private fun getMinSizeOfWinningMoves(): Int {
        return optimalWinningState?.pastMoves?.size ?: Int.MAX_VALUE
    }

    private fun getMinCostGameSolvingMoves(): List<Pair<CrosserIndices, Move>>? {
        while (activeGamePlayStates.isNotEmpty()) {
            for (currentState in activeGamePlayStates) {
                val nextStates = mutableListOf<GamePlayState>()
                val possibleMoves =
                    newGameSituationTeller(currentState.gamePlayPositions).getCurrentValidMoves()

                for ((crosserIndices, move) in possibleMoves) {
                    val newState = currentState.newStateAppliedMove(crosserIndices to move, rules)
                    if (isOptimalWinningSolution(newState)) {
                        optimalWinningState = newState
                        continue
                    }
                    if (newState.totalCost >= getMinWinningCost()) {
                        continue
                    }
                    val gamePlayPositions = newState.gamePlayPositions
                    if (newGameSituationTeller(gamePlayPositions).isGameOver()) {
                        continue
                    }

                    if (!activePositionsToMinCost.containsKey(newState.gamePlayPositions) || activePositionsToMinCost[newState.gamePlayPositions]!! > newState.totalCost) {
                        activePositionsToMinCost[gamePlayPositions] = newState.totalCost
                        nextStates.add(newState)
                    }
                }

                activeGamePlayStates.remove(currentState)
                activeGamePlayStates.addAll(nextStates)
            }
        }

        return optimalWinningState?.pastMoves
    }

    private fun isOptimalWinningSolution(newState: GamePlayState): Boolean {
        return newGameSituationTeller(newState.gamePlayPositions).isWon() && ((newState.totalCost == getMinWinningCost() && newState.pastMoves.size < getMinSizeOfWinningMoves()) || newState.totalCost < getMinWinningCost())
    }

    private fun newGameSituationTeller(positions: GamePlayPositions): GameSituationTeller {
        return GameSituationTeller(positions, rules)
    }
}
