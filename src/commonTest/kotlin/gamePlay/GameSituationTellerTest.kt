package gamePlay

import rules.*
import rules.classic.ClassicGameRules
import rules.classic.DAUGHTER
import rules.classic.FATHER
import rules.classic.MASTER
import kotlin.test.*

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
    fun `getCurrentValidMoves when two boat crossers in boat and one of them can drive boat on original river side and one crosser on target river side`() {
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

    @Test
    fun `getCurrentValidMoves when two crossers on target riverside and boat on that side too`() {
        val actualMoveSet =
            newGameSituationTeller(
                newGamePlayPositions(
                    crossers = listOf(
                        newClassicCrosser(
                            position = RiverCrosserPosition.TARGET_RIVERSIDE,
                        ),
                        newClassicCrosser(
                            position = RiverCrosserPosition.TARGET_RIVERSIDE,
                        ),
                    ),
                    boatPosition = BoatPosition.TARGET_RIVERSIDE,
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
    fun `getCurrentValidMoves when one crosser on original riverside and boat is not on that side`() {
        val actualMoveSet =
            newGameSituationTeller(
                newGamePlayPositions(
                    crossers = listOf(
                        newClassicCrosser(
                            position = RiverCrosserPosition.ORIGINAL_RIVERSIDE,
                        ),
                    ),
                    boatPosition = BoatPosition.TARGET_RIVERSIDE,
                ),
                rules = ClassicGameRules
            )
                .getCurrentValidMoves()
        assertEquals(emptySet(), actualMoveSet)
    }

    @Test
    fun `getCurrentValidMoves when crossers on boat can not drive`() {
        val actualMoveSet =
            newGameSituationTeller(
                newGamePlayPositions(
                    crossers = listOf(
                        newClassicCrosser(
                            position = RiverCrosserPosition.BOAT,
                            canDriveBoat = false
                        ),
                    ),
                ),
                rules = ClassicGameRules
            )
                .getCurrentValidMoves()
        val expectedMoveSet = setOf(
            setOf(0) to Move.TRANSIT,
        )
        assertEquals(expectedMoveSet, actualMoveSet)
    }

    @Test
    fun `getCurrentValidMoves when number of crossers on riverside is more than the capacity of boat`() {
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
            setOf(2) to Move.TRANSIT,
            setOf(0, 1) to Move.TRANSIT,
            setOf(0, 2) to Move.TRANSIT,
            setOf(1, 2) to Move.TRANSIT,
        )
        assertEquals(expectedMoveSet, actualMoveSet)
    }

    @Test
    fun `isGameOver when prohibited combination of crossers in the same position`() {
        val testCasePositions = RiverCrosserPosition.values()

        for (position in testCasePositions) {
            newGameSituationTeller(
                newGamePlayPositions(
                    crossers = listOf(
                        RiverCrosser(type = FATHER, position = position),
                        RiverCrosser(type = DAUGHTER, position = position),
                    ),
                ),
                rules = ClassicGameRules
            ).isGameOver()
                .let {
                    assertTrue(it)
                }
        }
    }

    @Test
    fun `isGameOver when allowed combination of crossers in the same position`() {
        val testCasePositions = RiverCrosserPosition.values()

        for (position in testCasePositions) {
            newGameSituationTeller(
                newGamePlayPositions(
                    crossers = listOf(
                        RiverCrosser(type = FATHER, position = position),
                        RiverCrosser(type = MASTER, position = position),
                    ),
                ),
                rules = ClassicGameRules
            ).isGameOver()
                .let {
                    assertFalse(it)
                }
        }
    }
}