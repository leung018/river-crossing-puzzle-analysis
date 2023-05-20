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
        ),
        ClassicGameRules
    )
}