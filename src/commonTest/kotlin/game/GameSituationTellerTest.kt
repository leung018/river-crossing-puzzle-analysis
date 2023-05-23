package game

import game.rules.*
import game.rules.classic.ClassicGameRules
import game.rules.classic.DAUGHTER
import game.rules.classic.FATHER
import game.rules.classic.MASTER
import testutil.newClassicCrosserType
import kotlin.test.*

private fun newClassicCrosser(position: RiverCrosserPosition, canDriveBoat: Boolean = false) =
    RiverCrosser(type = newClassicCrosserType(canDriveBoat), position)

private fun newGameSituationTeller(
    gameplayPositions: GamePlayPositions,
    rules: GameSituationRules = ClassicGameRules,
): GameSituationTeller {
    return GameSituationTeller(gameplayPositions, rules)
}

internal class GameSituationTellerTest {
    @Test
    fun `constructor when crossers list empty`() {
        assertFailsWith<IllegalArgumentException> {
            newGameSituationTeller(GamePlayPositions(crossers = listOf()))
        }
    }

    @Test
    fun `constructor when crossers list contain type that is not valid`() {
        val nonClassicType = RiverCrosserType("NOT_VALID")
        assertFailsWith<IllegalArgumentException> {
            newGameSituationTeller(
                GamePlayPositions(crossers = listOf(RiverCrosser(nonClassicType))),
                rules = ClassicGameRules
            )
        }
    }

    @Test
    fun `constructor when crossers list with more crossers on boat than boat capacity`() {
        assertFailsWith<IllegalArgumentException> {
            newGameSituationTeller(
                GamePlayPositions(
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
    fun `constructor when crossers list with equal crossers than boat capacity but total capacity of crosser exceed it`() {
        val fatGuy = RiverCrosserType("FAT_GUY", occupiedBoatCapacity = 2)
        val rules = object : GameRules by ClassicGameRules {
            override val validRiverCrosserTypes =
                ClassicGameRules.validRiverCrosserTypes + fatGuy
        }
        assertFailsWith<IllegalArgumentException> {
            newGameSituationTeller(
                GamePlayPositions(
                    crossers = listOf(
                        newClassicCrosser(RiverCrosserPosition.BOAT),
                        RiverCrosser(fatGuy, RiverCrosserPosition.BOAT)
                    )
                ),
                rules = rules
            )
        }
    }

    @Test
    fun `getCurrentValidMoves when two crossers on riverside and boat on that side too`() {
        data class TestCase(
            val boatPosition: BoatPosition,
            val crosserPosition: RiverCrosserPosition,
        )

        val testCases = listOf(
            TestCase(BoatPosition.ORIGINAL_RIVERSIDE, RiverCrosserPosition.ORIGINAL_RIVERSIDE),
            TestCase(BoatPosition.TARGET_RIVERSIDE, RiverCrosserPosition.TARGET_RIVERSIDE),
        )

        for (case in testCases) {
            val actualMoveSet =
                newGameSituationTeller(
                    GamePlayPositions(
                        crossers = listOf(
                            newClassicCrosser(position = case.crosserPosition),
                            newClassicCrosser(position = case.crosserPosition),
                        ),
                        boatPosition = case.boatPosition,
                    ),
                    rules = ClassicGameRules
                )
                    .getCurrentValidMoves()
            val expectedMoveSet = setOf(
                Move(setOf(0), MoveType.TRANSIT),
                Move(setOf(1), MoveType.TRANSIT),
                Move(setOf(0, 1), MoveType.TRANSIT)
            )
            assertEquals(expectedMoveSet, actualMoveSet)
        }
    }

    @Test
    fun `getCurrentValidMoves when two crossers on riverside but one of them occupied 2 boat capacity`() {
        val fatGuy = RiverCrosserType("FAT_GUY", occupiedBoatCapacity = 2)
        val rules = object : GameRules by ClassicGameRules {
            override val validRiverCrosserTypes =
                ClassicGameRules.validRiverCrosserTypes + fatGuy
        }

        val actualMoveSet =
            newGameSituationTeller(
                GamePlayPositions(
                    crossers = listOf(
                        newClassicCrosser(position = RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                        RiverCrosser(fatGuy, RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                    ),
                    boatPosition = BoatPosition.ORIGINAL_RIVERSIDE,
                ),
                rules = rules
            )
                .getCurrentValidMoves()

        val expectedMoveSet = setOf(
            Move(setOf(0), MoveType.TRANSIT),
            Move(setOf(1), MoveType.TRANSIT),
        )
        assertEquals(expectedMoveSet, actualMoveSet)
    }

    @Test
    fun `getCurrentValidMoves when one crosser on boat and two other crossers on riverside`() {
        val actualMoveSet =
            newGameSituationTeller(
                GamePlayPositions(
                    crossers = listOf(
                        newClassicCrosser(position = RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                        newClassicCrosser(position = RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                        newClassicCrosser(position = RiverCrosserPosition.BOAT, canDriveBoat = false),
                    ),
                    boatPosition = BoatPosition.ORIGINAL_RIVERSIDE,
                ),
                rules = ClassicGameRules
            )
                .getCurrentValidMoves()
        val expectedMoveSet = setOf(
            Move(setOf(0), MoveType.TRANSIT), // move to boat
            Move(setOf(1), MoveType.TRANSIT), // move to boat
            Move(setOf(2), MoveType.TRANSIT), // move to riverside
        )
        assertEquals(expectedMoveSet, actualMoveSet)
    }

    @Test
    fun `getCurrentValidMoves when one crosser on boat and one riverside crosser occupied 2 boat capacity on riverside`() {
        val fatGuy = RiverCrosserType("FAT_GUY", occupiedBoatCapacity = 2)
        val rules = object : GameRules by ClassicGameRules {
            override val validRiverCrosserTypes =
                ClassicGameRules.validRiverCrosserTypes + fatGuy
        }

        val actualMoveSet =
            newGameSituationTeller(
                GamePlayPositions(
                    crossers = listOf(
                        RiverCrosser(fatGuy, RiverCrosserPosition.TARGET_RIVERSIDE),
                        newClassicCrosser(position = RiverCrosserPosition.BOAT, canDriveBoat = false),
                    ),
                    boatPosition = BoatPosition.TARGET_RIVERSIDE,
                ),
                rules = rules
            )
                .getCurrentValidMoves()

        val expectedMoveSet = setOf(
            Move(setOf(1), MoveType.TRANSIT),
        )
        assertEquals(expectedMoveSet, actualMoveSet)

    }

    @Test
    fun `getCurrentValidMoves when two boat crossers in boat and one of them can drive boat on original river side and one crosser on target river side`() {
        val actualMoveSet =
            newGameSituationTeller(
                GamePlayPositions(
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
            Move(setOf(0), MoveType.TRANSIT),
            Move(setOf(1), MoveType.TRANSIT),
            Move(setOf(0, 1), MoveType.TRANSIT),
            Move(setOf(0, 1), MoveType.DRIVE_BOAT)
        )
        assertEquals(expectedMoveSet, actualMoveSet)
    }

    @Test
    fun `getCurrentValidMoves when one crosser on original riverside and boat is not on that side`() {
        val actualMoveSet =
            newGameSituationTeller(
                GamePlayPositions(
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
                GamePlayPositions(
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
            Move(setOf(0), MoveType.TRANSIT),
        )
        assertEquals(expectedMoveSet, actualMoveSet)
    }

    @Test
    fun `getCurrentValidMoves when number of crossers on riverside is more than the capacity of boat`() {
        val actualMoveSet =
            newGameSituationTeller(
                GamePlayPositions(
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
            Move(setOf(0), MoveType.TRANSIT),
            Move(setOf(1), MoveType.TRANSIT),
            Move(setOf(2), MoveType.TRANSIT),
            Move(setOf(0, 1), MoveType.TRANSIT),
            Move(setOf(0, 2), MoveType.TRANSIT),
            Move(setOf(1, 2), MoveType.TRANSIT),
        )
        assertEquals(expectedMoveSet, actualMoveSet)
    }

    @Test
    fun `isGameOver when prohibited combination of crossers in the same position`() {
        val testCasePositions = RiverCrosserPosition.values()

        for (position in testCasePositions) {
            newGameSituationTeller(
                GamePlayPositions(
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
                GamePlayPositions(
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

    @Test
    fun `isGameOver when prohibited combination of crossers in the same place but differ in position`() {
        data class TestCase(
            val positionA: RiverCrosserPosition,
            val positionB: RiverCrosserPosition,
            val boatPosition: BoatPosition
        )

        val testCases = listOf(
            TestCase(
                positionA = RiverCrosserPosition.ORIGINAL_RIVERSIDE,
                positionB = RiverCrosserPosition.BOAT,
                boatPosition = BoatPosition.ORIGINAL_RIVERSIDE
            ),
            TestCase(
                positionA = RiverCrosserPosition.TARGET_RIVERSIDE,
                positionB = RiverCrosserPosition.BOAT,
                boatPosition = BoatPosition.TARGET_RIVERSIDE
            ),
        )

        for (case in testCases) {
            newGameSituationTeller(
                GamePlayPositions(
                    crossers = listOf(
                        RiverCrosser(type = FATHER, position = case.positionA),
                        RiverCrosser(type = DAUGHTER, position = case.positionB),
                    ),
                    boatPosition = case.boatPosition,
                ),
                rules = object : GameRules by ClassicGameRules {
                    override val samePlaceMode: GameSituationRules.SamePlaceMode =
                        GameSituationRules.SamePlaceMode.BOAT_AND_NEARBY_RIVERSIDE_IN_SAME_PLACE
                }
            ).isGameOver()
                .let {
                    assertTrue(it)
                }
        }
    }

    @Test
    fun `isGameOver when allowed combination of crossers in the same place but differ in position`() {
        newGameSituationTeller(
            GamePlayPositions(
                crossers = listOf(
                    RiverCrosser(type = FATHER, position = RiverCrosserPosition.ORIGINAL_RIVERSIDE),
                    RiverCrosser(type = MASTER, position = RiverCrosserPosition.BOAT),
                ),
                boatPosition = BoatPosition.ORIGINAL_RIVERSIDE,
            ),
            rules = object : GameRules by ClassicGameRules {
                override val samePlaceMode: GameSituationRules.SamePlaceMode =
                    GameSituationRules.SamePlaceMode.BOAT_AND_NEARBY_RIVERSIDE_IN_SAME_PLACE
            }
        ).isGameOver()
            .let {
                assertFalse(it)
            }
    }

    @Test
    fun `isWon when all crossers are on target riverside`() {
        newGameSituationTeller(
            GamePlayPositions(
                crossers = listOf(
                    newClassicCrosser(
                        position = RiverCrosserPosition.TARGET_RIVERSIDE,
                    ),
                    newClassicCrosser(
                        position = RiverCrosserPosition.TARGET_RIVERSIDE,
                    ),
                ),
            ),
            rules = ClassicGameRules
        ).isWon()
            .let {
                assertTrue(it)
            }
    }

    @Test
    fun `isWon when not all crossers are on target riverside`() {
        newGameSituationTeller(
            GamePlayPositions(
                crossers = listOf(
                    newClassicCrosser(
                        position = RiverCrosserPosition.TARGET_RIVERSIDE,
                    ),
                    newClassicCrosser(
                        position = RiverCrosserPosition.ORIGINAL_RIVERSIDE,
                    ),
                ),
            ),
            rules = ClassicGameRules
        ).isWon()
            .let {
                assertFalse(it)
            }
    }
}