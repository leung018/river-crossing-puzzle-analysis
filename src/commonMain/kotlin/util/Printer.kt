package util

interface Printer {
    fun print(log: String)

    fun println(log: String = "") {
        print("$log\n")
    }
}

class ConsolePrinter : Printer {
    override fun print(log: String) {
        kotlin.io.print(log)
    }
}

class OutputTracePrinter : Printer {
    private var trace: String = ""

    override fun print(log: String) {
        trace += log
    }

    fun getOutputTrace(): String = trace
}