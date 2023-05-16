package gamePlay

import rules.Move
import rules.RiverCrosserType
import rules.classic.ClassicGameRules
import rules.classic.FATHER
import rules.classic.MOTHER
import rules.classic.SON
import testutil.assertIsWinAfterMoves
import kotlin.test.Test
import kotlin.test.assertEquals

class GamePlayBoardTest {

    @Test
    fun `test getMinCostGameSolvingMoves when crossers can reach target riverside`() {
        data class TestCase(
            val crosserTypes: List<RiverCrosserType>,
            val sampleAnswer: List<Pair<Set<Int>, Move>> // there may be more than one possible answer and the one returned from the function is one of them
        )

        val testCases = listOf(
            TestCase(
                crosserTypes = listOf(FATHER), sampleAnswer =
                listOf(
                    setOf(0) to Move.TRANSIT,
                    setOf(0) to Move.DRIVE_BOAT,
                    setOf(0) to Move.TRANSIT,
                )

            ),
            TestCase(
                crosserTypes = listOf(FATHER, SON), sampleAnswer =
                listOf(
                    setOf(0, 1) to Move.TRANSIT,
                    setOf(0, 1) to Move.DRIVE_BOAT,
                    setOf(0, 1) to Move.TRANSIT,
                )

            ),
            TestCase(
                crosserTypes = listOf(FATHER, SON, SON),
                sampleAnswer =
                listOf(
                    setOf(0, 1) to Move.TRANSIT,
                    setOf(0, 1) to Move.DRIVE_BOAT,
                    setOf(1) to Move.TRANSIT,
                    setOf(0) to Move.DRIVE_BOAT,
                    setOf(2) to Move.TRANSIT,
                    setOf(0, 2) to Move.DRIVE_BOAT,
                    setOf(0, 2) to Move.TRANSIT
                ),
            ),
            TestCase(
                crosserTypes = listOf(FATHER, MOTHER, SON),
                sampleAnswer = listOf(
                    setOf(0, 2) to Move.TRANSIT,
                    setOf(0, 2) to Move.DRIVE_BOAT,
                    setOf(2) to Move.TRANSIT,
                    setOf(0) to Move.DRIVE_BOAT,
                    setOf(1) to Move.TRANSIT,
                    setOf(0, 1) to Move.DRIVE_BOAT,
                    setOf(0, 1) to Move.TRANSIT
                )

            )
        )

        testCases.forEach { testCase ->
            val actualMoves = GamePlayBoard.getMinCostGameSolvingMoves(
                testCase.crosserTypes, ClassicGameRules
            )!!
            assertEquals(
                testCase.sampleAnswer.filter { it.second == Move.DRIVE_BOAT }.size,
                actualMoves.filter { it.second == Move.DRIVE_BOAT }.size,
            )
            assertEquals(
                testCase.sampleAnswer.size,
                actualMoves.size,
            )
            assertIsWinAfterMoves(
                GamePlayState(GamePlayPositions(testCase.crosserTypes)),
                actualMoves,
                ClassicGameRules
            )
        }
    }

    @Test
    fun `test getMinCostGameSolvingMoves when one crosser who cannot drive boat on original riverside`() {
        val actualMoves = GamePlayBoard.getMinCostGameSolvingMoves(
            listOf(
                SON
            ), ClassicGameRules
        )
        assertEquals(null, actualMoves)
    }

}