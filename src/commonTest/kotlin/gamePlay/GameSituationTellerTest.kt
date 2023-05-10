package gamePlay

import rules.*
import rules.classic.ClassicGameRules
import rules.classic.FATHER
import rules.classic.MASTER
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

private fun newGameSituationTeller(
    gameplayPositions: GamePlayPositions,
    rules: GameSituationRules = ClassicGameRules,
): GameSituationTeller {
    return GameSituationTeller(gameplayPositions, rules)
}

private fun newGamePlayPositions(
    crossers: List<RiverCrosser>,
    boatPosition: BoatPosition = BoatPosition.ORIGINAL_RIVERSIDE
): GamePlayPositions {
    return GamePlayPositions(crossers, boatPosition)
}

private fun newClassicCrosser(position: RiverCrosserPosition, canDriveBoat: Boolean = false) =
    RiverCrosser(type = if (canDriveBoat) FATHER else MASTER, position)

internal class GameSituationTellerTest {
    @Test
    fun `constructor when crossers list empty`() {
        assertFailsWith<IllegalArgumentException> {
            newGameSituationTeller(newGamePlayPositions(crossers = listOf()))
        }
    }

    @Test
    fun `constructor when crossers list contain type that is not valid`() {
        val nonClassicType = RiverCrosserType("NOT_VALID")
        assertFailsWith<IllegalArgumentException> {
            newGameSituationTeller(
                newGamePlayPositions(crossers = listOf(RiverCrosser(nonClassicType))),
                rules = ClassicGameRules
            )
        }
    }

    @Test
    fun `constructor when crossers list with more crossers on boat than boat capacity`() {
        assertFailsWith<IllegalArgumentException> {
            newGameSituationTeller(
                newGamePlayPositions(
                    crossers = listOf(
                        newClassicCrosser(RiverCrosserPosition.BOAT),
                        newClassicCrosser(RiverCrosserPosition.BOAT),
                        newClassicCrosser(RiverCrosserPosition.BOAT)
                    )
                ),
                rules = ClassicGameRules
            )
        }
    }

    @Test
    fun `getCurrentValidMoves when two crossers on original riverside and boat on that side too`() {
        val actualMoveSet =
            newGameSituationTeller(
                newGamePlayPositions(
                    crossers = listOf(
                        newClassicCrosser(
                            position = RiverCrosserPosition.ORIGINAL_RIVERSIDE,
                        ),
                        newClassicCrosser(
                            position = RiverCrosserPosition.ORIGINAL_RIVERSIDE,
                        ),
                    ),
                    boatPosition = BoatPosition.ORIGINAL_RIVERSIDE,
                ),
                rules = ClassicGameRules
            )
                .getCurrentValidMoves()
        val expectedMoveSet = setOf(
            setOf(0) to Move.TRANSIT,
            setOf(1) to Move.TRANSIT,
            setOf(0, 1) to Move.TRANSIT
        )
        assertEquals(expectedMoveSet, actualMoveSet)
    }

    @Test
    fun `getCurrentValidMoves when one boat driver crosser in boat`() {
        val actualMoveSet =
            newGameSituationTeller(
                newGamePlayPositions(
                    crossers = listOf(newClassicCrosser(RiverCrosserPosition.BOAT, canDriveBoat = true)),
                ),
                rules = ClassicGameRules
            )
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
                newGamePlayPositions(
                    crossers = listOf(
                        newClassicCrosser(RiverCrosserPosition.BOAT, canDriveBoat = true),
                        newClassicCrosser(RiverCrosserPosition.BOAT),
                        newClassicCrosser(RiverCrosserPosition.TARGET_RIVERSIDE)
                    ),
                    boatPosition = BoatPosition.ORIGINAL_RIVERSIDE,
                ),
                rules = ClassicGameRules
            )
                .getCurrentValidMoves()
        val expectedMoveSet = setOf(
            setOf(0) to Move.TRANSIT,
            setOf(1) to Move.TRANSIT,
            setOf(0, 1) to Move.TRANSIT,
            setOf(0, 1) to Move.DRIVE_BOAT
        )
        assertEquals(expectedMoveSet, actualMoveSet)
    }
}