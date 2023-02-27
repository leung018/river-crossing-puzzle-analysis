package gamePlay

import rules.BoatPosition
import rules.MoveType
import rules.RiverCrosserPosition
import rules.classic.ClassicGameRules
import rules.classic.FATHER
import rules.classic.MOTHER
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class GamePlayStateTest {
    @Test
    fun `newStateAppliedMoves when CrosserIndices out of original list range`() {
        val s = GamePlayState(CurrentPositions(listOf()), listOf(), 0)
        assertFailsWith<IllegalArgumentException> {
            s.newStateAppliedMoves(
                setOf(0) to Move(MoveType.TRANSIT),
                ClassicGameRules
            )
        }
    }

    @Test
    fun `newStateAppliedMoves when drive boat from original river side`() {
        val originalState =
            GamePlayState(
                CurrentPositions(
                    listOf(
                        RiverCrosser(FATHER),
                        RiverCrosser(MOTHER, RiverCrosserPosition.BOAT)
                    ),
                    boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
                ), listOf(), 0
            )
        val newState =
            originalState.newStateAppliedMoves(
                setOf(1) to Move(MoveType.DRIVE_BOAT),
                ClassicGameRules
            )
        val expectedState = GamePlayState(
            CurrentPositions(
                listOf(RiverCrosser(FATHER), RiverCrosser(MOTHER, RiverCrosserPosition.BOAT)),
                boatPosition = BoatPosition.TARGET_RIVERSIDE
            ),
            listOf((setOf(1) to Move(MoveType.DRIVE_BOAT))),
            1
        )

        assertEquals(expectedState, newState)
    }
}