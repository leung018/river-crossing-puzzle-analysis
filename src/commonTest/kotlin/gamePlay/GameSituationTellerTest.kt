package gamePlay

import rules.*
import rules.classic.ClassicGameRules
import rules.classic.FATHER
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

private fun newGameSituationTeller(
    crossers: List<RiverCrosser>,
    boatPosition: BoatPosition = BoatPosition.ORIGINAL_RIVERSIDE,
    rules: GameSituationRules = ClassicGameRules
): GameSituationTeller {
    return GameSituationTeller(crossers, boatPosition, rules)
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
            newGameSituationTeller(crossers = listOf(RiverCrosser(nonClassicType)), rules = ClassicGameRules)
        }
    }

    @Test
    fun `constructor when crossers list with more crossers on boat than boat capacity`() {
        assertFailsWith<IllegalArgumentException> {
            newGameSituationTeller(
                crossers = listOf(
                    newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.BOAT),
                    newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.BOAT),
                    newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.BOAT)
                )
            )
        }
    }

    @Test
    fun `getCurrentValidMoves when one boat driver crosser on ORIGINAL RIVER SIDE`() {
        val actualMoveSet =
            newGameSituationTeller(crossers = listOf(newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.ORIGINAL_RIVERSIDE)))
                .getCurrentValidMoves()
        val expectedMoveSet = setOf(
            setOf(0) to Move.TRANSIT
        )
        assertEquals(expectedMoveSet, actualMoveSet)
    }

    @Test
    fun `getCurrentValidMoves when one boat driver crosser in boat on original river side`() {
        val actualMoveSet =
            newGameSituationTeller(crossers = listOf(newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.BOAT)))
                .getCurrentValidMoves()

        val expectedMoveSet = setOf(
            setOf(0) to Move.TRANSIT,
            setOf(0) to Move.DRIVE_BOAT
        )
        assertEquals(expectedMoveSet, actualMoveSet)
    }

    @Test
    fun `getCurrentValidMoves when two boat driver crossers in boat on original river side and one crosser on target river side`() {
        val actualMoveSet =
            newGameSituationTeller(
                crossers = listOf(
                    newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.BOAT),
                    newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.BOAT),
                    newClassicRulesCrosserWhoCanDriveBoat(RiverCrosserPosition.TARGET_RIVERSIDE),
                ),
                boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
            ).getCurrentValidMoves()
        val expectedMoveSet = setOf(
            setOf(0) to Move.TRANSIT,
            setOf(1) to Move.TRANSIT,
            setOf(0, 1) to Move.DRIVE_BOAT
        )
        assertEquals(expectedMoveSet, actualMoveSet)
    }
}