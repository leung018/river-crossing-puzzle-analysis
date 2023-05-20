package game

import game.rules.BoatPosition
import game.rules.RiverCrosserPosition
import game.rules.classic.ClassicGameRules
import testutil.newClassicCrosserType
import util.OutputTracePrinter
import kotlin.test.Test
import kotlin.test.assertTrue

class SolutionPrinterTest {
    /**
     * Below tests won't check the exact output message completely because some messages will be considered as implementation detail of UI.
     * Tests don't need to couple with these implementation detail.
     */

    @Test
    fun `test printResult when no solution can be found`() {
        val outputTracePrinter = OutputTracePrinter()
        val solutionPrinter = SolutionPrinter(outputTracePrinter)
        solutionPrinter.printResult(listOf(newClassicCrosserType(canDriveBoat = false)), ClassicGameRules)

        val numberOfLines = outputTracePrinter.getOutputTrace().split("\n").size
        assertTrue(numberOfLines in 1..3) // Don't care what exact message is shown, but it should be short and fewer lines than logs from a game state.
    }

    @Test
    fun `test printResult when solution can be found`() {
        val solutionTracePrinter = OutputTracePrinter()
        val solutionPrinter = SolutionPrinter(solutionTracePrinter)

        fun getGamePlayStateOutputTrace(state: GamePlayState): String {
            val outputTracePrinter = OutputTracePrinter()
            GamePlayStatePrinter(outputTracePrinter).printState(state)
            return outputTracePrinter.getOutputTrace()
        }

        val boatDriverType = newClassicCrosserType(canDriveBoat = true)
        val initialCrosserTypes = listOf(boatDriverType)
        solutionPrinter.printResult(initialCrosserTypes, ClassicGameRules)

        val initialStateLog =
            getGamePlayStateOutputTrace(GamePlayState(GamePlayPositions(initialCrosserTypes), listOf(), 0))
        val winningStateLog = getGamePlayStateOutputTrace(
            GamePlayState(
                GamePlayPositions(
                    listOf(
                        RiverCrosser(
                            boatDriverType,
                            RiverCrosserPosition.TARGET_RIVERSIDE
                        )
                    ), BoatPosition.TARGET_RIVERSIDE
                ), GamePlayBoard.getMinCostGameSolvingMoves(initialCrosserTypes, ClassicGameRules)!!, 1
            )
        )
        assertTrue(solutionTracePrinter.getOutputTrace().contains(initialStateLog))
        assertTrue(solutionTracePrinter.getOutputTrace().contains(winningStateLog))
    }
}