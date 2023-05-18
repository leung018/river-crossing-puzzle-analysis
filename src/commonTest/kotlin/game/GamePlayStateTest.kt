package game

import game.rules.BoatPosition
import game.rules.MoveType
import game.rules.RiverCrosserPosition
import game.rules.classic.ClassicGameRules
import game.rules.classic.FATHER
import game.rules.classic.MOTHER
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class GamePlayStateTest {
    @Test
    fun `newStateAppliedMoves when crosserIndices out of original list range`() {
        val s = GamePlayState(GamePlayPositions(listOf<RiverCrosser>()))
        assertFailsWith<IllegalArgumentException> {
            s.newStateAppliedMove(
                Move(setOf(0), MoveType.TRANSIT)
            )
        }
    }

    @Test
    fun `newStateAppliedMoves when crosserIndices are in different position`() {
        val s = GamePlayState(
            GamePlayPositions(
                listOf(
                    RiverCrosser(FATHER, RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                    RiverCrosser(MOTHER, RiverCrosserPosition.BOAT),
                )
            )
        )
        assertFailsWith<IllegalArgumentException> {
            s.newStateAppliedMove(
                Move(setOf(0, 1), MoveType.TRANSIT)
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
                Move(setOf(1), MoveType.DRIVE_BOAT),
                ClassicGameRules
            )
        val expectedState = GamePlayState(
            GamePlayPositions(
                listOf(RiverCrosser(FATHER), RiverCrosser(MOTHER, RiverCrosserPosition.BOAT)),
                boatPosition = BoatPosition.TARGET_RIVERSIDE
            ),
            listOf(Move(setOf(1), MoveType.DRIVE_BOAT)),
            1
        )

        assertEquals(expectedState, newState)
    }

    @Test
    fun `newStateAppliedMoves when two crossers transit from riverside to boat`() {
        val originalState =
            GamePlayState(
                GamePlayPositions(
                    listOf(
                        RiverCrosser(FATHER, RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                        RiverCrosser(MOTHER, RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                    )
                )
            )
        val newState =
            originalState.newStateAppliedMove(
                Move(setOf(0, 1), MoveType.TRANSIT),
            )
        val expectedState = GamePlayState(
            GamePlayPositions(
                listOf(
                    RiverCrosser(FATHER, RiverCrosserPosition.BOAT),
                    RiverCrosser(MOTHER, RiverCrosserPosition.BOAT),
                )
            ),
            listOf(Move(setOf(0, 1), MoveType.TRANSIT)),
            0
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
                Move(setOf(0), MoveType.DRIVE_BOAT),
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
                Move(setOf(0), MoveType.TRANSIT),
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
                Move(setOf(0), MoveType.TRANSIT),
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
                Move(setOf(0), MoveType.TRANSIT),
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
                Move(setOf(0), MoveType.TRANSIT),
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
                Move(setOf(0), MoveType.DRIVE_BOAT),
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
                Move(setOf(0), MoveType.TRANSIT),
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
                Move(setOf(0), MoveType.TRANSIT),
                ClassicGameRules
            )
        }
    }
}