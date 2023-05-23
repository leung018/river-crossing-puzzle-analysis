package game

import game.rules.*
import util.getCombinations

/**
 * @throws IllegalArgumentException if the input gamePlayPositions is invalid.
 */
class GameSituationTeller(private val gamePlayPositions: GamePlayPositions, private val rules: GameSituationRules) {
    init {
        validateCrossers()
    }

    private fun validateCrossers() {
        if (gamePlayPositions.crossers.isEmpty()) {
            throw IllegalArgumentException("Input crossers cannot be empty")
        }
        validateCrosserTypes()
        validateCrosserPositions()
    }

    private fun validateCrosserTypes() {
        for (crosser in gamePlayPositions.crossers) {
            if (!rules.validRiverCrosserTypes.contains(crosser.type)) {
                throw IllegalArgumentException("Crossers contain type that does not exist in rules")
            }
        }
    }

    private fun validateCrosserPositions() {
        val totalOccupiedCapacity = gamePlayPositions.crossers.filter { it.position == RiverCrosserPosition.BOAT }
            .sumOf { it.type.occupiedBoatCapacity }
        if (totalOccupiedCapacity > rules.boatCapacity) {
            throw IllegalArgumentException("Boat capacity is not enough for crossers on boat")
        }
    }

    /**
     * @return set of moves that can be applied to a valid game state that will not game over.
     */
    fun getCurrentValidMoves(): Set<Move> {
        return getCurrentPossibleMoves().filter {
            GameSituationTeller(gamePlayPositions.newPositionsAppliedMove(it), rules)
                .isGameOver().not()
        }.toSet()
    }

    /**
     * @return set of moves that can be applied to a valid game state.
     * But it doesn't mean that the game state after applying the move will not game over.
     */
    fun getCurrentPossibleMoves(): Set<Move> {
        val newMoves = mutableSetOf<Move>()

        // moves of crossers on boat
        val crosserIndicesOnBoat = getCrossersIndicesOfPosition(RiverCrosserPosition.BOAT)
        crosserIndicesOnBoat.let {
            if (it.isNotEmpty()) {
                // moves that drive boat
                if (canDriveBoat(it)) {
                    newMoves.add(
                        Move(it, MoveType.DRIVE_BOAT)
                    )
                }

                // moves that transit to riverside
                val maxCrossersOfTransitToRiverside = if (rules.transitOneCrosserOnly) {
                    1
                } else {
                    it.size
                }
                getCombinations(it, maxCrossersOfTransitToRiverside).forEach { possibleIndices ->
                    newMoves.add(
                        Move(possibleIndices, MoveType.TRANSIT)
                    )
                }
            }
        }

        // moves of crossers on riverside
        val maxCrossersOfTransitToBoat = if (rules.transitOneCrosserOnly) {
            1
        } else {
            rules.boatCapacity - crosserIndicesOnBoat.size
        }
        getCrossersIndicesOfPosition(gamePlayPositions.boatPosition.nearbyRiversideForCrosser()).let {
            if (it.isNotEmpty()) {
                getCombinations(
                    it,
                    maxCrossersOfTransitToBoat
                ).forEach { possibleTransitIndices ->
                    if (sumOfOccupiedBoatCapacity(possibleTransitIndices + crosserIndicesOnBoat) <= rules.boatCapacity) {
                        newMoves.add(
                            Move(possibleTransitIndices, MoveType.TRANSIT)
                        )
                    }
                }
            }
        }

        return newMoves
    }

    private fun getCrossersIndicesOfPosition(targetPosition: RiverCrosserPosition): Set<Int> {
        val crosserIndices = mutableSetOf<Int>()
        for ((index, crosser) in gamePlayPositions.crossers.withIndex()) {
            if (crosser.position == targetPosition) {
                crosserIndices.add(index)
            }
        }
        return crosserIndices
    }

    private fun sumOfOccupiedBoatCapacity(crosserIndices: Set<Int>): Int {
        return crosserIndices.map { index -> gamePlayPositions.crossers[index] }
            .sumOf { crosser -> crosser.type.occupiedBoatCapacity }
    }

    private fun canDriveBoat(crosserIndices: Set<Int>): Boolean {
        return crosserIndices.map { index -> gamePlayPositions.crossers[index] }
            .any { crosser -> rules.boatDriverTypes.contains(crosser.type) }
    }

    fun isWon(): Boolean {
        return gamePlayPositions.crossers.all { it.position == RiverCrosserPosition.TARGET_RIVERSIDE }
    }

    /**
     * @param lastMoveType the last move type that was applied to the game state.
     * In some rules, if last move type is DRIVE_BOAT, may affect whether it is game over or not.
     */
    fun isGameOver(lastMoveType: MoveType = MoveType.TRANSIT): Boolean {
        val places: List<Set<RiverCrosserPosition>> =
            when (rules.samePlaceMode) {
                GameSituationRules.SamePlaceMode.BOAT_AND_NEARBY_RIVERSIDE_IN_SAME_PLACE ->
                    listOf(
                        setOf(gamePlayPositions.boatPosition.nearbyRiversideForCrosser(), RiverCrosserPosition.BOAT),
                        setOf(gamePlayPositions.boatPosition.opposite().nearbyRiversideForCrosser())
                    )

                GameSituationRules.SamePlaceMode.BOAT_AND_RIVERSIDE_IN_DIFFERENT_PLACE ->
                    RiverCrosserPosition.values().map { setOf(it) }
            }

        for (place in places) {
            val crossers = filterCrossersAt(place)
            if (!canGameContinue(crossers)) {
                return true
            }
        }

        if (lastMoveType == MoveType.DRIVE_BOAT && rules.samePlaceMode == GameSituationRules.SamePlaceMode.BOAT_AND_NEARBY_RIVERSIDE_IN_SAME_PLACE) {
            if (!canGameContinue(filterCrossersAt(setOf(RiverCrosserPosition.BOAT)))) {
                return true
            }
        }

        return false
    }

    private fun filterCrossersAt(positions: Set<RiverCrosserPosition>): List<RiverCrosser> {
        return gamePlayPositions.crossers.filter { positions.contains(it.position) }
    }

    private fun canGameContinue(crossersInSamePlace: List<RiverCrosser>): Boolean {
        return rules.canGameContinue(crossersInSamePlace.map(RiverCrosser::type))
    }
}