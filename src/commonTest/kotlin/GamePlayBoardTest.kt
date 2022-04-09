import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import RiverCrosserPosition.*

internal class GamePlayStateTest {
    @Test
    fun `newStateAppliedMoves when CrosserIndices is not valid`() {
        val s = GamePlayState(listOf(), listOf(), 0)
        assertFailsWith<IllegalArgumentException> {
            s.newStateAppliedMoves(
                setOf(0) to Move(MoveType.TRANSIT, BOAT_ON_TARGET_RIVER_SIDE),
                ClassicGameRules
            )
        }
    }

    @Test
    fun `newStateAppliedMoves when input move is Valid`() {
        val originalState =
            GamePlayState(listOf(RiverCrosser(FATHER), RiverCrosser(MOTHER, BOAT_ON_ORIGINAL_RIVER_SIZE)), listOf(), 0)
        val newState =
            originalState.newStateAppliedMoves(
                setOf(1) to Move(MoveType.DRIVE_BOAT, BOAT_ON_TARGET_RIVER_SIDE),
                ClassicGameRules
            )
        val expectedState = GamePlayState(
            listOf(RiverCrosser(FATHER), RiverCrosser(MOTHER, BOAT_ON_TARGET_RIVER_SIDE)),
            listOf((setOf(1) to Move(MoveType.DRIVE_BOAT, BOAT_ON_TARGET_RIVER_SIDE))),
            1
        )

        assertEquals(expectedState, newState)
    }
}