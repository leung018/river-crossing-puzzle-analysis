package game


interface Printer {
    fun print(log: String)
}

class ConsolePrinter : Printer {
    override fun print(log: String) {
        println(log)
    }
}

class OutputTracePrinter : Printer {
    private val traces: MutableList<String> = mutableListOf()

    override fun print(log: String) {
        traces.add(log)
    }

    fun getOutputTraces(): List<String> = traces
}

class GamePlayStatePrinter(private val printer: Printer) {
    fun print(gamePlayState: GamePlayState) {
        TODO()
    }
}