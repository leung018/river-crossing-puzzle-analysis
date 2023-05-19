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
        val solvingMoves = GamePlayBoard.getMinCostGameSolvingMoves(
            initialCrosserTypes,
            rules
        )

        if (solvingMoves != null) {
            printer.println("Optimal solution found for the specified crosser types and rules!")

            // Print introduction
            printer.println()
            printer.println("The following game state will be shown after each move, along with the last move made to achieve it.")
            printer.println("DRIVE_BOAT indicates that the boat has been driven from one riverside to another, while TRANSIT indicates movement from one riverside to the boat or vice versa.")
            printer.println("The total cost of moves used to reach the current game state will also be specified, with the cost of each move type according to the specified game rules.")
            printer.println()

            var state = GamePlayState(GamePlayPositions(initialCrosserTypes))
            gamePlayStatePrinter.printState(state)
            solvingMoves.forEach {
                state = state.newStateAppliedMove(it, rules)
                printer.println()
                gamePlayStatePrinter.printState(state)
            }
        } else {
            printer.println("No solution found for the specified input crosser types and rules!")
        }
    }

}