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

internal class GamePlayPositionsTest {
    @Test
    fun `newPositionsAppliedMoves when crosserIndices out of original list range`() {
        val p = GamePlayPositions(listOf<RiverCrosser>())
        assertFailsWith<IllegalArgumentException> {
            p.newPositionsAppliedMove(
                Move(setOf(0), MoveType.TRANSIT)
            )
        }
    }

    @Test
    fun `newPositionsAppliedMoves when crosserIndices are in different position`() {
        val p = GamePlayPositions(
            listOf(
                RiverCrosser(FATHER, RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                RiverCrosser(MOTHER, RiverCrosserPosition.BOAT),
            )
        )
        assertFailsWith<IllegalArgumentException> {
            p.newPositionsAppliedMove(
                Move(setOf(0, 1), MoveType.TRANSIT)
            )
        }
    }

    @Test
    fun `newPositionsAppliedMoves when one crosser drive boat from original riverside and one crosser stay at riverside`() {
        val originalPositions = GamePlayPositions(
            listOf(
                RiverCrosser(FATHER, RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                RiverCrosser(MOTHER, RiverCrosserPosition.BOAT)
            ),
            boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
        )
        val newPositions =
            originalPositions.newPositionsAppliedMove(
                Move(setOf(1), MoveType.DRIVE_BOAT),
            )
        val expectedPositions =
            GamePlayPositions(
                listOf(RiverCrosser(FATHER), RiverCrosser(MOTHER, RiverCrosserPosition.BOAT)),
                boatPosition = BoatPosition.TARGET_RIVERSIDE
            )

        assertEquals(expectedPositions, newPositions)
    }

    @Test
    fun `newPositionsAppliedMoves when two crossers transit from riverside to boat`() {
        val originalPositions =
            GamePlayPositions(
                listOf(
                    RiverCrosser(FATHER, RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                    RiverCrosser(MOTHER, RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                )
            )
        val newPositions =
            originalPositions.newPositionsAppliedMove(
                Move(setOf(0, 1), MoveType.TRANSIT),
            )
        val expectedPositions = GamePlayPositions(
            listOf(
                RiverCrosser(FATHER, RiverCrosserPosition.BOAT),
                RiverCrosser(MOTHER, RiverCrosserPosition.BOAT),
            )
        )

        assertEquals(expectedPositions, newPositions)
    }

    @Test
    fun `newPositionsAppliedMoves when drive boat from target riverside`() {
        val originalPositions =
            GamePlayPositions(
                listOf(
                    RiverCrosser(MOTHER, RiverCrosserPosition.BOAT)
                ),
                boatPosition = BoatPosition.TARGET_RIVERSIDE
            )
        val newPositions =
            originalPositions.newPositionsAppliedMove(
                Move(setOf(0), MoveType.DRIVE_BOAT),
            )

        assertEquals(
            GamePlayPositions(
                listOf(RiverCrosser(MOTHER, RiverCrosserPosition.BOAT)),
                boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
            ), newPositions
        )
    }

    @Test
    fun `newPositionsAppliedMoves when transit from original riverside to boat`() {
        val originalPositions =
            GamePlayPositions(
                listOf(
                    RiverCrosser(FATHER, RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                ),
                boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
            )
        val newPositions =
            originalPositions.newPositionsAppliedMove(
                Move(setOf(0), MoveType.TRANSIT),
            )

        assertEquals(
            GamePlayPositions(
                listOf(
                    RiverCrosser(FATHER, RiverCrosserPosition.BOAT),
                ),
                boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
            ), newPositions
        )
    }

    @Test
    fun `newPositionsAppliedMoves when transit from target riverside to boat`() {
        val originalPositions =
            GamePlayPositions(
                listOf(
                    RiverCrosser(FATHER, RiverCrosserPosition.TARGET_RIVERSIDE),
                ),
                boatPosition = BoatPosition.TARGET_RIVERSIDE
            )
        val newPositions =
            originalPositions.newPositionsAppliedMove(
                Move(setOf(0), MoveType.TRANSIT),
            )

        assertEquals(
            GamePlayPositions(
                listOf(
                    RiverCrosser(FATHER, RiverCrosserPosition.BOAT),
                ),
                boatPosition = BoatPosition.TARGET_RIVERSIDE
            ), newPositions
        )
    }

    @Test
    fun `newPositionsAppliedMoves when transit from boat in original riverside`() {
        val originalPositions =
            GamePlayPositions(
                listOf(
                    RiverCrosser(FATHER, RiverCrosserPosition.BOAT),
                ),
                boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
            )
        val newPositions =
            originalPositions.newPositionsAppliedMove(
                Move(setOf(0), MoveType.TRANSIT),
            )

        assertEquals(
            GamePlayPositions(
                listOf(
                    RiverCrosser(FATHER, RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                ),
                boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
            ), newPositions
        )
    }

    @Test
    fun `newPositionsAppliedMoves when transit from boat in target riverside`() {
        val originalPositions =
            GamePlayPositions(
                listOf(
                    RiverCrosser(FATHER, RiverCrosserPosition.BOAT),
                ),
                boatPosition = BoatPosition.TARGET_RIVERSIDE
            )
        val newPositions =
            originalPositions.newPositionsAppliedMove(
                Move(setOf(0), MoveType.TRANSIT),
            )

        assertEquals(
            GamePlayPositions(
                listOf(
                    RiverCrosser(FATHER, RiverCrosserPosition.TARGET_RIVERSIDE),
                ),
                boatPosition = BoatPosition.TARGET_RIVERSIDE
            ), newPositions
        )
    }

    @Test
    fun `newPositionsAppliedMoves when drive boat move for riverside crosser`() {
        val originalPositions =
            GamePlayPositions(
                listOf(
                    RiverCrosser(FATHER, RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                    RiverCrosser(MOTHER, RiverCrosserPosition.BOAT),
                ),
                boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
            )
        assertFailsWith<IllegalArgumentException> {
            originalPositions.newPositionsAppliedMove(
                Move(setOf(0), MoveType.DRIVE_BOAT),
            )
        }
    }

    @Test
    fun `newPositionsAppliedMoves when transit for original riverside crosser but boat is on the other side`() {
        val originalPositions =
            GamePlayPositions(
                listOf(
                    RiverCrosser(FATHER, RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                ),
                boatPosition = BoatPosition.TARGET_RIVERSIDE
            )
        assertFailsWith<IllegalArgumentException> {
            originalPositions.newPositionsAppliedMove(
                Move(setOf(0), MoveType.TRANSIT),
            )
        }
    }

    @Test
    fun `newPositionsAppliedMoves when transit for target riverside crosser but boat is on the other side`() {
        val originalPositions =
            GamePlayPositions(
                listOf(
                    RiverCrosser(FATHER, RiverCrosserPosition.TARGET_RIVERSIDE),
                ),
                boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
            )
        assertFailsWith<IllegalArgumentException> {
            originalPositions.newPositionsAppliedMove(
                Move(setOf(0), MoveType.TRANSIT),
            )
        }
    }
}

class GamePlayStateTest {
    @Test
    fun `newStateAppliedMoves when added move is TRANSIT`() {
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
            originalState.gamePlayPositions.newPositionsAppliedMove(Move(setOf(1), MoveType.DRIVE_BOAT)),
            listOf(Move(setOf(1), MoveType.DRIVE_BOAT)),
            1
        )

        assertEquals(expectedState, newState)
    }

    @Test
    fun `newStateAppliedMoves when added move is DRIVE_BOAT`() {
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
            originalState.gamePlayPositions.newPositionsAppliedMove(Move(setOf(0, 1), MoveType.TRANSIT)),
            listOf(Move(setOf(0, 1), MoveType.TRANSIT)),
            0
        )

        assertEquals(expectedState, newState)
    }
}