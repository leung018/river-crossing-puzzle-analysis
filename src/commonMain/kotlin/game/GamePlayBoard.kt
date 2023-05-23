package game

import game.rules.GameRules
import game.rules.RiverCrosserType

/**
 * Will compute the optimal solution when its instance is created.
 */
class GamePlayBoard private constructor(gamePlayPositions: GamePlayPositions, private val rules: GameRules) {

    companion object {
        /**
         * @return the optimal solution for the game. Null if no solution exists.
         * Optimal solution is the one with minimum cost and minimum number of moves.
         */
        fun getMinCostGameSolvingMoves(
            initialCrosserTypes: List<RiverCrosserType>,
            rules: GameRules
        ): List<Move>? {
            return GamePlayBoard(
                GamePlayPositions(initialCrosserTypes),
                rules
            ).optimalWinningState?.pastMoves
        }

    }

    init {
        // active means neither game over nor game win.
        val activeStateQueue = ArrayDeque<GamePlayState>()
        activeStateQueue.add(GamePlayState(gamePlayPositions))
        val activePositionsToMinCost: MutableMap<GamePlayPositions, Int> = mutableMapOf(
            gamePlayPositions to 0
        )

        // Compute optimal winning state. Each state will be considered like a vertex in graph and this graph will be traversed in BFS order.
        // Since in BFS order, the first winning state found within the same total cost will also have minimum number of moves.
        while (activeStateQueue.isNotEmpty()) {
            val currentState = activeStateQueue.removeFirst()
            val nextStates = mutableListOf<GamePlayState>()
            val validMoves =
                newGameSituationTeller(currentState.gamePlayPositions).getCurrentValidMoves()

            // Exploring neighbour states from currentState.
            for (move in validMoves) {
                val newState = currentState.newStateAppliedMove(move, rules)
                val newPositions = newState.gamePlayPositions
                if (newGameSituationTeller(newPositions).isWon() && newState.totalCost < getMinWinningCost()) {
                    optimalWinningState = newState
                    continue
                }
                if (newState.totalCost >= getMinWinningCost()) {
                    continue
                }

                if (!activePositionsToMinCost.containsKey(newPositions) || activePositionsToMinCost[newPositions]!! > newState.totalCost) {
                    activePositionsToMinCost[newPositions] = newState.totalCost
                    nextStates.add(newState)
                }
            }

            activeStateQueue.addAll(nextStates)
        }
    }

    /**
     *  optimalWinningState is the state that using the minimum sizes of moves to achieve the minimum cost of solutions.
     */
    private var optimalWinningState: GamePlayState? = null

    private fun getMinWinningCost(): Int {
        return optimalWinningState?.totalCost ?: Int.MAX_VALUE
    }

    private fun newGameSituationTeller(positions: GamePlayPositions): GameSituationTeller {
        return GameSituationTeller(positions, rules)
    }
}
