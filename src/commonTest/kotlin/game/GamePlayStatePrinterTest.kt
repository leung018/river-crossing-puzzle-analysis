package game

import game.rules.BoatPosition
import game.rules.MoveType
import game.rules.RiverCrosserPosition
import game.rules.RiverCrosserType
import util.OutputTracePrinter
import util.trimAllLines
import kotlin.test.Test
import kotlin.test.assertEquals

class GamePlayStatePrinterTest {
    @Test
    fun `test printState`() {
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
                ), expectedOutput = """
                    Round: 1
                    ---------------------------------
                    Original riverside | HANDSOME(#1)
                    River              | <>(Boat)
                    River              |
                    Target riverside   | PRETTY(#2)
                    ---------------------------------
                    Total cost: 0
                    Last move: 
                """
            ),
            TestCase(
                gamePlayState = GamePlayState(
                    gamePlayPositions = GamePlayPositions(
                        crossers = listOf(
                            RiverCrosser(RiverCrosserType("HANDSOME"), RiverCrosserPosition.BOAT),
                            RiverCrosser(RiverCrosserType("PRETTY"), RiverCrosserPosition.BOAT),
                        ),
                        boatPosition = BoatPosition.TARGET_RIVERSIDE
                    ),
                    pastMoves = listOf(
                        Move(setOf(1, 0), MoveType.TRANSIT),
                        Move(setOf(1, 0), MoveType.DRIVE_BOAT),
                    ),
                    totalCost = 1,
                ), expectedOutput = """
                    Round: 3
                    -----------------------------------------------------
                    Original riverside |
                    River              |
                    River              | <HANDSOME(#1), PRETTY(#2)>(Boat)
                    Target riverside   |
                    -----------------------------------------------------
                    Total cost: 1
                    Last move: #1, #2 DRIVE_BOAT
                """
            ),
            TestCase(
                gamePlayState = GamePlayState(
                    gamePlayPositions = GamePlayPositions(
                        crossers = listOf(
                            RiverCrosser(RiverCrosserType("HANDSOME"), RiverCrosserPosition.BOAT),
                        ),
                        boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
                    ),
                    pastMoves = listOf(
                    ),
                    totalCost = 0,
                ),
                expectedOutput = """
                    Round: 1
                    -----------------------------------------
                    Original riverside |
                    River              | <HANDSOME(#1)>(Boat)
                    River              |
                    Target riverside   |
                    -----------------------------------------
                    Total cost: 0
                    Last move: 
                """
            )
        )

        for (testCase in testCases) {
            val printer = OutputTracePrinter()
            val gamePlayStatePrinter = GamePlayStatePrinter(printer)
            gamePlayStatePrinter.printState(
                testCase.gamePlayState
            )
            assertEquals(testCase.expectedOutput.trimIndent().trimAllLines() + "\n", printer.getOutputTrace())
        }
    }
}