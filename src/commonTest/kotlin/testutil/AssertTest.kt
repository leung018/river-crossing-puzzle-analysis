package testutil

import game.GamePlayPositions
import game.GamePlayState
import game.Move
import game.RiverCrosser
import game.rules.BoatPosition
import game.rules.MoveType
import game.rules.RiverCrosserPosition
import game.rules.classic.ClassicGameRules
import game.rules.classic.FATHER
import kotlin.test.Test
import kotlin.test.assertFailsWith

class AssertIsWonAfterMovesTest {
    @Test
    fun `test assertIsWonAfterMoves when is not win`() {
        assertFailsWith<AssertionError> {
            assertIsWonAfterMoves(
                GamePlayState(
                    GamePlayPositions(
                        listOf(
                            RiverCrosser(
                                FATHER,
                                RiverCrosserPosition.ORIGINAL_RIVERSIDE
                            )
                        ),
                        BoatPosition.ORIGINAL_RIVERSIDE
                    ),
                ),
                listOf(Move(setOf(0), MoveType.TRANSIT)),
                ClassicGameRules
            )
        }
    }

    @Test
    fun `test assertIsWonAfterMoves when is win`() {
        assertIsWonAfterMoves(
            GamePlayState(
                GamePlayPositions(
                    listOf(
                        RiverCrosser(
                            FATHER,
                            RiverCrosserPosition.ORIGINAL_RIVERSIDE
                        )
                    ),
                    BoatPosition.ORIGINAL_RIVERSIDE
                ),
            ),
            listOf(
                Move(setOf(0), MoveType.TRANSIT),
                Move(setOf(0), MoveType.DRIVE_BOAT),
                Move(setOf(0), MoveType.TRANSIT)
            ),
            ClassicGameRules
        )
    }

    @Test
    fun `test assertIsWonAfterMoves when moves is empty`() {
        assertIsWonAfterMoves(
            GamePlayState(
                GamePlayPositions(
                    listOf(
                        RiverCrosser(
                            FATHER,
                            RiverCrosserPosition.TARGET_RIVERSIDE
                        )
                    ),
                ),
            ),
            listOf(),
            ClassicGameRules
        )
    }
}