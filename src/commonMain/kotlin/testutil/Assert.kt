package testutil

import gamePlay.CrosserIndices
import gamePlay.GamePlayState
import gamePlay.GameSituationTeller
import rules.GameSituationRules
import kotlin.test.assertTrue

fun assertIsWinAfterMoves(
    initialState: GamePlayState,
    moves: List<Pair<CrosserIndices, rules.Move>>,
    rules: GameSituationRules,
    message: String? = null,
) {
    var state = initialState
    moves.forEach { move ->
        state = state.newStateAppliedMove(move)
    }
    assertTrue(
        GameSituationTeller(gamePlayPositions = state.gamePlayPositions, rules = rules).isWin(),
        message ?: "Expected win after applied moves but is not. InitialState: $initialState, moves: $moves"
    )
}