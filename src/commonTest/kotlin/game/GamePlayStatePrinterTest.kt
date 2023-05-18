package game

import game.rules.BoatPosition
import game.rules.RiverCrosserPosition
import game.rules.RiverCrosserType
import kotlin.test.Test
import kotlin.test.assertEquals

class GamePlayStatePrinterTest {
    @Test
    fun `test print`() {
        data class TestCase(
            val gamePlayState: GamePlayState,
            val expectedOutput: String
        )

        val testCases = listOf(
            TestCase(
                gamePlayState = GamePlayState(
                    gamePlayPositions = GamePlayPositions(
                        crossers = listOf(
                            RiverCrosser(RiverCrosserType("HANDSOME"), RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                            RiverCrosser(RiverCrosserType("PRETTY"), RiverCrosserPosition.TARGET_RIVERSIDE),
                        ),
                        boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
                    ),
                    pastMoves = listOf(),
                    totalCost = 0,
                ), expectedOutput = "TODO"
            ),
        )

        for (testCase in testCases) {
            val printer = OutputTracePrinter()
            val gamePlayStatePrinter = GamePlayStatePrinter(printer)
            gamePlayStatePrinter.print(
                testCase.gamePlayState
            )
            printer.getOutputTraces().last().let { trace ->
                assertEquals(testCase.expectedOutput, trace)
            }
        }
    }
}