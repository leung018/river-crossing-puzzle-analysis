import game.SolutionPrinter
import game.rules.classic.*

fun main() {
    SolutionPrinter().printResult(
        listOf(
            FATHER,
            MOTHER,
            SON,
            DAUGHTER,
            DAUGHTER,
            MASTER,
            DOG
        ), // TODO: need bug fix. Expecting solution is found but result is not actually
        ClassicGameRules
    )
}