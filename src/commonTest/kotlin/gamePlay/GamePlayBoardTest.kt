package gamePlay

import rules.Move
import rules.classic.ClassicGameRules
import rules.classic.FATHER
import rules.classic.SON
import kotlin.test.Test
import kotlin.test.assertEquals

class GamePlayBoardTest {

    @Test
    fun `test when one crosser who can drive boat on original riverside`() {
        val actualMoves = GamePlayBoard.getMinCostGameSolvingMoves(
            listOf(
                FATHER
            ), ClassicGameRules
        )
        val expectedMoves = setOf(
            listOf(
                setOf(0) to Move.TRANSIT,
                setOf(0) to Move.DRIVE_BOAT,
                setOf(0) to Move.TRANSIT,
            )
        )
        assertEquals(expectedMoves, actualMoves)
    }

    @Test
    fun `test when one crosser who cannot drive boat on original riverside`() {
        val actualMoves = GamePlayBoard.getMinCostGameSolvingMoves(
            listOf(
                SON
            ), ClassicGameRules
        )
        assertEquals(emptySet(), actualMoves)
    }

}