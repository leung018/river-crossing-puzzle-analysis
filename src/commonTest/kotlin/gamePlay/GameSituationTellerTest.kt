package gamePlay

import rules.GameSituationRules
import rules.MoveType
import rules.RiverCrosserPosition
import rules.RiverCrosserType
import rules.classic.ClassicGameRules
import rules.classic.FATHER
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

private fun newGameSituationTeller(
    crossers: List<RiverCrosser>,
    rules: GameSituationRules = ClassicGameRules
): GameSituationTeller {
    return GameSituationTeller(crossers, rules)
}

private fun newClassicRulesCrosserWhoCanDriveBoat(position: RiverCrosserPosition) =
    RiverCrosser(type = FATHER, position)

internal class GameSituationTellerTest {
    @Test
    fun `constructor when crossers list empty`() {
        assertFailsWith<IllegalArgumentException> {
            newGameSituationTeller(crossers = listOf())
        }
    }

    @Test
    fun `constructor when crossers list contain type that is not valid`() {
        val nonClassicType = RiverCrosserType("NOT_VALID")
        assertFailsWith<IllegalArgumentException> {
            newGameSituationTeller(crossers = listOf(RiverCrosser(nonClassicType)), ClassicGameRules)
        }
    }

    @Test
    fun `constructor when crossers list with more crossers on boat than boat capacity`() {
        assertFailsWith<IllegalArgumentException> {
            newGameSituationTeller(
                crossers = listOf(
                    newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.BOAT_ON_TARGET_RIVER_SIDE),
                    newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.BOAT_ON_TARGET_RIVER_SIDE),
                    newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.BOAT_ON_TARGET_RIVER_SIDE)
                )
            )
        }
        assertFailsWith<IllegalArgumentException> {
            newGameSituationTeller(
                crossers = listOf(
                    newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.BOAT_ON_ORIGINAL_RIVER_SIZE),
                    newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.BOAT_ON_ORIGINAL_RIVER_SIZE),
                    newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.BOAT_ON_ORIGINAL_RIVER_SIZE)
                )
            )
        }
    }

    @Test
    fun `constructor when crossers list with two crosser on different boat position`() {
        assertFailsWith<IllegalArgumentException> {
            newGameSituationTeller(
                crossers = listOf(
                    newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.BOAT_ON_ORIGINAL_RIVER_SIZE),
                    newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.BOAT_ON_TARGET_RIVER_SIDE),
                )
            )
        }
    }

    @Test
    fun `getCurrentValidMoves when one boat driver crosser on ORIGINAL RIVER SIDE`() {
        val actualMoveSet =
            newGameSituationTeller(crossers = listOf(newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.ORIGINAL_RIVER_SIDE)))
                .getCurrentValidMoves()
        val expectedMoveSet = setOf(
            setOf(0) to Move(
                MoveType.TRANSIT,
                RiverCrosserPosition.BOAT_ON_ORIGINAL_RIVER_SIZE
            )
        )
        assertEquals(expectedMoveSet, actualMoveSet)
    }

    @Test
    fun `getCurrentValidMoves when one boat driver crosser in boat on original river side`() {
        val actualMoveSet =
            newGameSituationTeller(crossers = listOf(newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.BOAT_ON_ORIGINAL_RIVER_SIZE)))
                .getCurrentValidMoves()

        val expectedMoveSet = setOf(
            setOf(0) to Move(
                MoveType.TRANSIT,
                RiverCrosserPosition.ORIGINAL_RIVER_SIDE
            ),
            setOf(0) to Move(
                MoveType.DRIVE_BOAT,
                RiverCrosserPosition.BOAT_ON_TARGET_RIVER_SIDE
            )
        )
        assertEquals(expectedMoveSet, actualMoveSet)
    }

    @Test
    fun `getCurrentValidMoves when two boat driver crossers in boat on original river side and one crosser on target river side`() {
        val actualMoveSet =
            newGameSituationTeller(
                crossers = listOf(
                    newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.BOAT_ON_ORIGINAL_RIVER_SIZE),
                    newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.BOAT_ON_ORIGINAL_RIVER_SIZE),
                    newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.TARGET_RIVER_SIDE),
                )
            ).getCurrentValidMoves()
        val expectedMoveSet = setOf(
            setOf(0) to Move(
                MoveType.TRANSIT,
                RiverCrosserPosition.ORIGINAL_RIVER_SIDE
            ),
            setOf(1) to Move(
                MoveType.TRANSIT,
                RiverCrosserPosition.ORIGINAL_RIVER_SIDE
            ),
            setOf(0, 1) to Move(
                MoveType.DRIVE_BOAT,
                RiverCrosserPosition.BOAT_ON_TARGET_RIVER_SIDE
            )
        )
        assertEquals(expectedMoveSet, actualMoveSet)
    }
}