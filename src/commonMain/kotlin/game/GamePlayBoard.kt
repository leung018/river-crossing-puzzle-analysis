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
        val activeGamePlayStates = mutableSetOf(GamePlayState(gamePlayPositions))
        val activePositionsToMinCost: MutableMap<GamePlayPositions, Int> = mutableMapOf(
            gamePlayPositions to 0
        )

        // compute optimal winning state.
        while (activeGamePlayStates.isNotEmpty()) {
            for (currentState in activeGamePlayStates) {
                val nextStates = mutableListOf<GamePlayState>()
                val possibleMoves =
                    newGameSituationTeller(currentState.gamePlayPositions).getCurrentValidMoves()

                for (move in possibleMoves) {
                    val newState = currentState.newStateAppliedMove(move, rules)
                    if (isOptimalWinningSolution(newState)) {
                        optimalWinningState = newState
                        continue
                    }
                    if (newState.totalCost >= getMinWinningCost()) {
                        continue
                    }

                    val newPositions = newState.gamePlayPositions
                    if (newGameSituationTeller(newPositions).isGameOver()) {
                        println("currentPosition: ${currentState.gamePlayPositions.crossers.map { it.position }} boatPosition: ${currentState.gamePlayPositions.boatPosition}")
                        println("pastMoves: ${currentState.pastMoves}")
                        continue
                    }

                    if (!activePositionsToMinCost.containsKey(newPositions) || activePositionsToMinCost[newPositions]!! > newState.totalCost) {
                        activePositionsToMinCost[newPositions] = newState.totalCost
                        nextStates.add(newState)
                    }
                }

                activeGamePlayStates.remove(currentState)
                activeGamePlayStates.addAll(nextStates)
            }
        }
    }

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

    private fun isOptimalWinningSolution(newState: GamePlayState): Boolean {
        return newGameSituationTeller(newState.gamePlayPositions).isWon() && ((newState.totalCost == getMinWinningCost() && newState.pastMoves.size < getMinSizeOfWinningMoves()) || newState.totalCost < getMinWinningCost())
    }

    private fun newGameSituationTeller(positions: GamePlayPositions): GameSituationTeller {
        return GameSituationTeller(positions, rules)
    }
}
