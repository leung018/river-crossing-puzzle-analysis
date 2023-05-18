import gamePlay.GamePlayBoard
import rules.classic.*

fun main() {
    println("Compute the minimum cost game solving moves for the classic game:")
    println(
        GamePlayBoard.getMinCostGameSolvingMoves(
            listOf(FATHER, MOTHER, SON, DAUGHTER, DAUGHTER, MASTER, DOG),
            ClassicGameRules
        )
    )
}