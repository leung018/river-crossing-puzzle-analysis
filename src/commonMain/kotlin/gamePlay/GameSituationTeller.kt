package gamePlay

import rules.*
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
        val inBoatCount = gamePlayPositions.crossers.count { it.position == RiverCrosserPosition.BOAT }
        if (inBoatCount > rules.boatCapacity) {
            throw IllegalArgumentException("More crossers are in the boat than its capacity")
        }
    }

    /**
     * @return set of moves that can be applied to a valid game state.
     * But it doesn't mean that the game state after applying the move will not game over.
     */
    fun getCurrentValidMoves(): Set<Move> {
        val newMoves = mutableSetOf<Move>()

        // moves of crossers on boat
        val numOfCrossersOnBoat: Int
        getCrossersIndicesOfPosition(RiverCrosserPosition.BOAT).let {
            numOfCrossersOnBoat = it.size
            if (it.isNotEmpty()) {
                // moves that drive boat
                if (canDriveBoat(it)) {
                    newMoves.add(
                        Move(it, MoveType.DRIVE_BOAT)
                    )
                }

                // moves that transit to riverside
                getCombinations(it, it.size).forEach { possibleIndices ->
                    newMoves.add(
                        Move(possibleIndices, MoveType.TRANSIT)
                    )
                }
            }
        }

        // moves of crossers on riverside
        getCrossersIndicesOfPosition(gamePlayPositions.boatPosition.nearbyRiversideForCrosser()).let {
            if (it.isNotEmpty()) {
                getCombinations(
                    it,
                    rules.boatCapacity - numOfCrossersOnBoat
                ).forEach { possibleIndices ->
                    newMoves.add(
                        Move(possibleIndices, MoveType.TRANSIT)
                    )
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

    private fun canDriveBoat(crosserIndices: Set<Int>): Boolean {
        return crosserIndices.map { index -> gamePlayPositions.crossers[index] }
            .any { crosser -> rules.boatDriverTypes.contains(crosser.type) }
    }

    fun isWon(): Boolean {
        return gamePlayPositions.crossers.all { it.position == RiverCrosserPosition.TARGET_RIVERSIDE }
    }

    fun isGameOver(): Boolean {
        val places: List<Set<RiverCrosserPosition>> = if (rules.areBoatAndNearByRiversideInSamePlace) {
            listOf(
                setOf(gamePlayPositions.boatPosition.nearbyRiversideForCrosser(), RiverCrosserPosition.BOAT),
                setOf(gamePlayPositions.boatPosition.opposite().nearbyRiversideForCrosser())
            )
        } else {
            RiverCrosserPosition.values().map { setOf(it) }
        }

        for (place in places) {
            val crossers = filterCrossersAt(place)
            if (!canGameContinue(crossers)) {
                return true
            }
        }
        return false
    }

    private fun filterCrossersAt(positions: Set<RiverCrosserPosition>): Set<RiverCrosser> {
        return gamePlayPositions.crossers.filter { positions.contains(it.position) }.toSet()
    }

    private fun canGameContinue(crossersInSamePlace: Set<RiverCrosser>): Boolean {
        return rules.canGameContinue(crossersInSamePlace.map(RiverCrosser::type).toSet())
    }
}