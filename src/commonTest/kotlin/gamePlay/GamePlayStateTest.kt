package gamePlay

import rules.BoatPosition
import rules.Move
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
        val s = GamePlayState(GamePlayPositions(listOf<RiverCrosser>()))
        assertFailsWith<IllegalArgumentException> {
            s.newStateAppliedMove(
                setOf(0) to Move.TRANSIT
            )
        }
    }

    @Test
    fun `newStateAppliedMoves when one crosser drive boat from original riverside and one crosser stay at riverside`() {
        val originalState =
            GamePlayState(
                GamePlayPositions(
                    listOf(
                        RiverCrosser(FATHER, RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                        RiverCrosser(MOTHER, RiverCrosserPosition.BOAT)
                    ),
                    boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
                ), listOf(), 0
            )
        val newState =
            originalState.newStateAppliedMove(
                setOf(1) to Move.DRIVE_BOAT,
                ClassicGameRules
            )
        val expectedState = GamePlayState(
            GamePlayPositions(
                listOf(RiverCrosser(FATHER), RiverCrosser(MOTHER, RiverCrosserPosition.BOAT)),
                boatPosition = BoatPosition.TARGET_RIVERSIDE
            ),
            listOf((setOf(1) to Move.DRIVE_BOAT)),
            1
        )

        assertEquals(expectedState, newState)
    }

    @Test
    fun `newStateAppliedMoves when drive boat from target riverside`() {
        val originalState =
            GamePlayState(
                GamePlayPositions(
                    listOf(
                        RiverCrosser(MOTHER, RiverCrosserPosition.BOAT)
                    ),
                    boatPosition = BoatPosition.TARGET_RIVERSIDE
                )
            )
        val newState =
            originalState.newStateAppliedMove(
                setOf(0) to Move.DRIVE_BOAT,
            )

        assertEquals(
            GamePlayPositions(
                listOf(RiverCrosser(MOTHER, RiverCrosserPosition.BOAT)),
                boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
            ), newState.gamePlayPositions
        )
    }

    @Test
    fun `newStateAppliedMoves when transit from original riverside to boat`() {
        val originalState =
            GamePlayState(
                GamePlayPositions(
                    listOf(
                        RiverCrosser(FATHER, RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                    ),
                    boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
                )
            )
        val newState =
            originalState.newStateAppliedMove(
                setOf(0) to Move.TRANSIT,
            )

        assertEquals(
            GamePlayPositions(
                listOf(
                    RiverCrosser(FATHER, RiverCrosserPosition.BOAT),
                ),
                boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
            ), newState.gamePlayPositions
        )
    }

    @Test
    fun `newStateAppliedMoves when transit from target riverside to boat`() {
        val originalState =
            GamePlayState(
                GamePlayPositions(
                    listOf(
                        RiverCrosser(FATHER, RiverCrosserPosition.TARGET_RIVERSIDE),
                    ),
                    boatPosition = BoatPosition.TARGET_RIVERSIDE
                )
            )
        val newState =
            originalState.newStateAppliedMove(
                setOf(0) to Move.TRANSIT,
                ClassicGameRules
            )

        assertEquals(
            GamePlayPositions(
                listOf(
                    RiverCrosser(FATHER, RiverCrosserPosition.BOAT),
                ),
                boatPosition = BoatPosition.TARGET_RIVERSIDE
            ), newState.gamePlayPositions
        )
    }

    @Test
    fun `newStateAppliedMoves when transit from boat in original riverside`() {
        val originalState =
            GamePlayState(
                GamePlayPositions(
                    listOf(
                        RiverCrosser(FATHER, RiverCrosserPosition.BOAT),
                    ),
                    boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
                )
            )
        val newState =
            originalState.newStateAppliedMove(
                setOf(0) to Move.TRANSIT,
                ClassicGameRules
            )

        assertEquals(
            GamePlayPositions(
                listOf(
                    RiverCrosser(FATHER, RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                ),
                boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
            ), newState.gamePlayPositions
        )
    }

    @Test
    fun `newStateAppliedMoves when transit from boat in target riverside`() {
        val originalState =
            GamePlayState(
                GamePlayPositions(
                    listOf(
                        RiverCrosser(FATHER, RiverCrosserPosition.BOAT),
                    ),
                    boatPosition = BoatPosition.TARGET_RIVERSIDE
                )
            )
        val newState =
            originalState.newStateAppliedMove(
                setOf(0) to Move.TRANSIT,
                ClassicGameRules
            )

        assertEquals(
            GamePlayPositions(
                listOf(
                    RiverCrosser(FATHER, RiverCrosserPosition.TARGET_RIVERSIDE),
                ),
                boatPosition = BoatPosition.TARGET_RIVERSIDE
            ), newState.gamePlayPositions
        )
    }

    @Test
    fun `newStateAppliedMoves when drive boat move for riverside crosser`() {
        val originalState =
            GamePlayState(
                GamePlayPositions(
                    listOf(
                        RiverCrosser(FATHER, RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                        RiverCrosser(MOTHER, RiverCrosserPosition.BOAT),
                    ),
                    boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
                )
            )
        assertFailsWith<IllegalArgumentException> {
            originalState.newStateAppliedMove(
                setOf(0) to Move.DRIVE_BOAT,
                ClassicGameRules
            )
        }
    }

    @Test
    fun `newStateAppliedMoves when transit for original riverside crosser but boat is on the other side`() {
        val originalState =
            GamePlayState(
                GamePlayPositions(
                    listOf(
                        RiverCrosser(FATHER, RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                    ),
                    boatPosition = BoatPosition.TARGET_RIVERSIDE
                )
            )
        assertFailsWith<IllegalArgumentException> {
            originalState.newStateAppliedMove(
                setOf(0) to Move.TRANSIT,
                ClassicGameRules
            )
        }
    }

    @Test
    fun `newStateAppliedMoves when transit for target riverside crosser but boat is on the other side`() {
        val originalState =
            GamePlayState(
                GamePlayPositions(
                    listOf(
                        RiverCrosser(FATHER, RiverCrosserPosition.TARGET_RIVERSIDE),
                    ),
                    boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
                )
            )
        assertFailsWith<IllegalArgumentException> {
            originalState.newStateAppliedMove(
                setOf(0) to Move.TRANSIT,
                ClassicGameRules
            )
        }
    }
}