package testutil

import gamePlay.GamePlayState
import gamePlay.GameSituationTeller
import gamePlay.Move
import rules.GameSituationRules
import kotlin.test.assertTrue

fun assertIsWonAfterMoves(
    initialState: GamePlayState,
    moves: List<Move>,
    rules: GameSituationRules,
    message: String? = null,
) {
    var state = initialState
    moves.forEach { move ->
        state = state.newStateAppliedMove(move)
    }
    assertTrue(
        GameSituationTeller(gamePlayPositions = state.gamePlayPositions, rules = rules).isWon(),
        message ?: "Expected win after applied moves but is not. InitialState: $initialState, moves: $moves"
    )
}