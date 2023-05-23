package game

import game.rules.GameRules
import game.rules.RiverCrosserType
import util.ConsolePrinter
import util.Printer

class SolutionPrinter(private val printer: Printer = ConsolePrinter()) {
    private val gamePlayStatePrinter = GamePlayStatePrinter(printer)

    /**
     * Find and then print the optimal solution if exists. Otherwise, will also print a message to indicate that no solution found.
     */
    fun printResult(
        initialCrosserTypes: List<RiverCrosserType>,
        rules: GameRules
    ) {
        val solvingMoves = SolutionFinder.computeMinCostGameSolvingMoves(
            initialCrosserTypes,
            rules
        )

        if (solvingMoves != null) {
            printer.println("Optimal solution found for the specified crosser types and rules!")

            // Print introduction
            printer.println()
            printer.println("The program will display the game state after each move, along with the last move made to achieve it.")
            printer.println("DRIVE_BOAT indicates that the boat has been driven from one riverside to another, while TRANSIT indicates movement from one riverside to the boat or vice versa.")
            printer.println("The game rules will also configure the cost of DRIVE_BOAT and TRANSIT, determine whether the riverside and nearby boat are considered different places.")
            printer.println()

            var state = GamePlayState(GamePlayPositions(initialCrosserTypes))
            gamePlayStatePrinter.printState(state)
            solvingMoves.forEach {
                state = state.newStateAppliedMove(it, rules)
                printer.println()
                gamePlayStatePrinter.printState(state)
            }

            printer.println()
            printer.println("Finished!")
        } else {
            printer.println("No solution found for the specified input crosser types and rules!")
        }
    }

}