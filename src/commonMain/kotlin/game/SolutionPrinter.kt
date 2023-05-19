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
            printer.println("Below will shown the game state after each move.")
            printer.println("Each game state shown will also state the last move to achieve it. DRIVE_BOAT means the boat is driven from one riverside to the another riverside. TRANSIT means from one riverside to boat or vice versa.")
            printer.println("It also specify the total cost of moves used to reach the current game state. The cost of different type of move is according to the specified game rules.")
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