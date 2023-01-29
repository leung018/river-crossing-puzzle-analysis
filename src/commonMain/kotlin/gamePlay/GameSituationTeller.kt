package gamePlay

import rules.GameSituationRules
import rules.MoveType
import rules.RiverCrosserPosition

class GameSituationTeller(private val crossers: List<RiverCrosser>, val rules: GameSituationRules) {
    init {
        validateCrossers()
    }

    private fun validateCrossers() {
        if (crossers.isEmpty()) {
            throw IllegalArgumentException("Input crossers cannot be empty")
        }
        validateCrossersType()
        validateCrossersPosition()
    }

    private fun validateCrossersType() {
        for (crosser in crossers) {
            if (!rules.validRiverCrosserTypes.contains(crosser.type)) {
                throw IllegalArgumentException("Crossers contain type that rules not exist")
            }
        }
    }

    private fun validateCrossersPosition() {
        var inOriginalRiverSizeBoatCount = 0
        var inTargetRiverSizeBoatCount = 0
        for (crosser in crossers) {
            if (crosser.position == RiverCrosserPosition.BOAT_ON_ORIGINAL_RIVER_SIZE) {
                inOriginalRiverSizeBoatCount++
            }
            if (crosser.position == RiverCrosserPosition.BOAT_ON_TARGET_RIVER_SIDE) {
                inTargetRiverSizeBoatCount++
            }
        }

        if (inOriginalRiverSizeBoatCount > 0 && inTargetRiverSizeBoatCount > 0) {
            throw IllegalArgumentException("Crossers cannot in boat on different river size at the same time")
        }

        val inBoatCount = inOriginalRiverSizeBoatCount + inTargetRiverSizeBoatCount
        if (inBoatCount > rules.boatCapacity) {
            throw IllegalArgumentException("More crossers are in the boat than its capacity")
        }
    }

    fun getCurrentValidMoves(): Set<Pair<CrosserIndices, Move>> {
        val newMoves = mutableSetOf<Pair<CrosserIndices, Move>>()
        for ((index, crosser) in crossers.withIndex()) {
            if (crosser.position == RiverCrosserPosition.ORIGINAL_RIVER_SIDE) {
                newMoves.add(setOf(index) to Move(MoveType.TRANSIT, RiverCrosserPosition.BOAT_ON_ORIGINAL_RIVER_SIZE))
            } else if (crosser.position == RiverCrosserPosition.BOAT_ON_ORIGINAL_RIVER_SIZE) {
                newMoves.add(
                    getCrossersIndicesOfPosition(RiverCrosserPosition.BOAT_ON_ORIGINAL_RIVER_SIZE) to Move(
                        MoveType.DRIVE_BOAT,
                        RiverCrosserPosition.BOAT_ON_TARGET_RIVER_SIDE
                    )
                )
                newMoves.add(setOf(index) to Move(MoveType.TRANSIT, RiverCrosserPosition.ORIGINAL_RIVER_SIDE))
            }
        }
        return newMoves
    }

    private fun getCrossersIndicesOfPosition(targetPosition: RiverCrosserPosition): CrosserIndices {
        val crosserIndices = mutableSetOf<Int>()
        for ((index, crosser) in crossers.withIndex()) {
            if (crosser.position == targetPosition) {
                crosserIndices.add(index)
            }
        }
        return crosserIndices
    }

    fun isWin(): Boolean {
        TODO()
    }

    fun isGameOver(): Boolean {
        TODO()
    }
}