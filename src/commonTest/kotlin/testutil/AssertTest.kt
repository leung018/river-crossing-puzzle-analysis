package testutil

import gamePlay.GamePlayPositions
import gamePlay.GamePlayState
import gamePlay.RiverCrosser
import rules.BoatPosition
import rules.RiverCrosserPosition
import rules.classic.ClassicGameRules
import rules.classic.FATHER
import kotlin.test.Test
import kotlin.test.assertFailsWith

class AssertisWonAfterMovesTest {
    @Test
    fun `test assertisWonAfterMoves when is not win`() {
        assertFailsWith<AssertionError> {
            assertisWonAfterMoves(
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
                listOf(setOf(0) to rules.Move.TRANSIT),
                ClassicGameRules
            )
        }
    }

    @Test
    fun `test assertisWonAfterMove when is win`() {
        assertisWonAfterMoves(
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
                setOf(0) to rules.Move.TRANSIT,
                setOf(0) to rules.Move.DRIVE_BOAT,
                setOf(0) to rules.Move.TRANSIT
            ),
            ClassicGameRules
        )
    }

    @Test
    fun `test assertisWonAfterMove when moves is empty`() {
        assertisWonAfterMoves(
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