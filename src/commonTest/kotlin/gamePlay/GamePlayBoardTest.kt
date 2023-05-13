package gamePlay

import rules.Move
import rules.RiverCrosserType
import rules.classic.ClassicGameRules
import rules.classic.FATHER
import rules.classic.SON
import kotlin.test.Test
import kotlin.test.assertEquals

class GamePlayBoardTest {

    @Test
    fun `test getMinCostGameSolvingMoves when crossers can reach target riverside`() {
        data class TestCase(
            val crosserTypes: List<RiverCrosserType>,
            val expectedMoves: Set<List<Pair<Set<Int>, Move>>>
        )

        val testCases = listOf(
            TestCase(
                crosserTypes = listOf(FATHER), expectedMoves = setOf(
                    listOf(
                        setOf(0) to Move.TRANSIT,
                        setOf(0) to Move.DRIVE_BOAT,
                        setOf(0) to Move.TRANSIT,
                    )
                )
            ),
            TestCase(
                crosserTypes = listOf(FATHER, SON), expectedMoves = setOf(
                    listOf(
                        setOf(0, 1) to Move.TRANSIT,
                        setOf(0) to Move.DRIVE_BOAT,
                        setOf(0, 1) to Move.TRANSIT,
                    )
                )
            ),
        )

        testCases.forEach { testCase ->
            val actualMoves = GamePlayBoard.getMinCostGameSolvingMoves(
                testCase.crosserTypes, ClassicGameRules
            )
            assertEquals(testCase.expectedMoves, actualMoves)
        }
    }

    @Test
    fun `test getMinCostGameSolvingMoves when one crosser who cannot drive boat on original riverside`() {
        val actualMoves = GamePlayBoard.getMinCostGameSolvingMoves(
            listOf(
                SON
            ), ClassicGameRules
        )
        assertEquals(emptySet(), actualMoves)
    }

}