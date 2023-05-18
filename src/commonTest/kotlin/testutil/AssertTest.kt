package testutil

import gamePlay.GamePlayPositions
import gamePlay.GamePlayState
import gamePlay.Move
import gamePlay.RiverCrosser
import rules.BoatPosition
import rules.MoveType
import rules.RiverCrosserPosition
import rules.classic.ClassicGameRules
import rules.classic.FATHER
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