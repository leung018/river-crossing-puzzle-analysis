package game

import game.rules.BoatPosition
import game.rules.RiverCrosserPosition


interface Printer {
    fun print(log: String)
}

class ConsolePrinter : Printer {
    override fun print(log: String) {
        print(log)
    }
}

class OutputTracePrinter : Printer {
    private var trace: String = ""

    override fun print(log: String) {
        trace += log
    }

    fun getOutputTrace(): String = trace
}

class GamePlayStatePrinter(private val printer: Printer) {
    fun printLog(gamePlayState: GamePlayState) {
        val gamePlayPositionsLog = gamePlayPositionsLog(gamePlayState.gamePlayPositions)
        myPrintln(gamePlayPositionsLog)
        printDashLine(gamePlayPositionsLog.longestLineLength())
        myPrintln("Total cost: ${gamePlayState.totalCost}")
        myPrintln("Last move: ${gamePlayState.pastMoves.lastOrNull()?.let { moveLog(it) } ?: ""}")
    }

    private fun myPrintln(log: String) {
        printer.print(log + "\n")
    }

    private fun gamePlayPositionsLog(gamePlayPositions: GamePlayPositions): String {
        val crossers = gamePlayPositions.crossers

        return """
            Original riverside |${
            crossers.logOfPositionAt(RiverCrosserPosition.ORIGINAL_RIVERSIDE).addLeadingSpaceIfNotEmpty()
        }
            River              |${if (gamePlayPositions.boatPosition == BoatPosition.ORIGINAL_RIVERSIDE) " ${crossers.logOfBoat()}" else ""}
            River              |${if (gamePlayPositions.boatPosition == BoatPosition.TARGET_RIVERSIDE) " ${crossers.logOfBoat()}" else ""}
            Target riverside   |${
            crossers.logOfPositionAt(RiverCrosserPosition.TARGET_RIVERSIDE).addLeadingSpaceIfNotEmpty()
        }
        """.trimIndent()
    }

    private fun String.addLeadingSpaceIfNotEmpty(): String {
        return if (this.isNotEmpty()) {
            " $this"
        } else {
            this
        }
    }

    private fun List<RiverCrosser>.logOfBoat(): String = "<${this.logOfPositionAt(RiverCrosserPosition.BOAT)}>(Boat)"

    private fun List<RiverCrosser>.logOfPositionAt(position: RiverCrosserPosition): String {
        val tokens = mutableListOf<String>()
        for ((index, crosser) in this.withIndex()) {
            if (crosser.position == position) {
                tokens.add("${crosser.type.id}(#${index + 1})")
            }
        }
        return tokens.joinToString(", ")
    }

    private fun String.longestLineLength(): Int {
        return split("\n").maxOfOrNull { it.length } ?: 0
    }

    private fun printDashLine(length: Int) {
        myPrintln("-".repeat(length))
    }

    private fun moveLog(move: Move): String {
        return "${move.crosserIndices.sorted().joinToString(", ") { "#${it + 1}" }} ${move.type}"
    }

}