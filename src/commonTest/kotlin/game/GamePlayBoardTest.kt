package game

import game.rules.MoveType
import game.rules.RiverCrosserType
import game.rules.classic.ClassicGameRules
import game.rules.classic.FATHER
import game.rules.classic.MOTHER
import game.rules.classic.SON
import testutil.assertIsWonAfterMoves
import kotlin.test.Test
import kotlin.test.assertEquals

class GamePlayBoardTest {

    @Test
    fun `test getMinCostGameSolvingMoves when crossers can reach target riverside`() {
        data class TestCase(
            val crosserTypes: List<RiverCrosserType>,
            val expectedTotalCost: Int,
            val expectedNumOfMoves: Int,
        )

        val testCases = listOf(
            TestCase(
                crosserTypes = listOf(FATHER),
                expectedTotalCost = 1,
                expectedNumOfMoves = 3,
                // sample answer:
                // Move(setOf(0), MoveType.TRANSIT),
                // Move(setOf(0), MoveType.DRIVE_BOAT),
                // Move(setOf(0), MoveType.TRANSIT),
            ),
            TestCase(
                crosserTypes = listOf(FATHER, SON),
                expectedTotalCost = 1,
                expectedNumOfMoves = 3,
                // sample answer:
                // Move(setOf(0, 1), MoveType.TRANSIT),
                // Move(setOf(0, 1), MoveType.DRIVE_BOAT),
                // Move(setOf(0, 1), MoveType.TRANSIT),

            ),
            TestCase(
                crosserTypes = listOf(FATHER, SON, SON),
                expectedTotalCost = 3,
                expectedNumOfMoves = 7,
                // sample answer:
                // Move(setOf(0, 1), MoveType.TRANSIT),
                // Move(setOf(0, 1), MoveType.DRIVE_BOAT),
                // Move(setOf(1), MoveType.TRANSIT),
                // Move(setOf(0), MoveType.DRIVE_BOAT),
                // Move(setOf(2), MoveType.TRANSIT),
                // Move(setOf(0, 2), MoveType.DRIVE_BOAT),
                // Move(setOf(0, 2), MoveType.TRANSIT)
            ),
            TestCase(
                crosserTypes = listOf(FATHER, MOTHER, SON),
                expectedTotalCost = 3,
                expectedNumOfMoves = 7,
                // sample answer:
                // Move(setOf(0, 2), MoveType.TRANSIT),
                // Move(setOf(0, 2), MoveType.DRIVE_BOAT),
                // Move(setOf(2), MoveType.TRANSIT),
                // Move(setOf(0), MoveType.DRIVE_BOAT),
                // Move(setOf(1), MoveType.TRANSIT),
                // Move(setOf(0, 1), MoveType.DRIVE_BOAT),
                // Move(setOf(0, 1), MoveType.TRANSIT)
            )
        )

        testCases.forEach { testCase ->
            val actualMoves = GamePlayBoard.getMinCostGameSolvingMoves(
                testCase.crosserTypes, ClassicGameRules
            )!!
            assertEquals(
                testCase.expectedTotalCost,
                actualMoves.filter { it.type == MoveType.DRIVE_BOAT }.size,
            )
            assertEquals(
                testCase.expectedNumOfMoves,
                actualMoves.size,
            )
            assertIsWonAfterMoves(
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