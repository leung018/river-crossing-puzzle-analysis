package game.rules


data class RiverCrosserType(
    val id: String,
    val occupiedBoatCapacity: Int = 1
)
// Use data class instead of enum because for future feature if crosser type is defined by external config

interface GameRules : GameSituationRules, MoveTypeCostRules

interface GameSituationRules {

    /**
     * Define which set of crossers are considered as same place.
     *
     * e.g. Given that there are three crossers A, B and C. A and B will fight together and cause game over if they are in the same place
     * and C is not there. If A and B are on the boat and C is on the nearby riverside, different modes will have different result.
     *
     * - BOAT_AND_RIVERSIDE_IN_DIFFERENT_PLACE: A and B will fight together and cause game over.
     * - BOAT_AND_NEARBY_RIVERSIDE_IN_SAME_PLACE: A and B will not fight together and game can continue. But If next move of A and B is drive_boat, will cause game over after this move.
     */
    enum class SamePlaceMode {
        BOAT_AND_RIVERSIDE_IN_DIFFERENT_PLACE,
        BOAT_AND_NEARBY_RIVERSIDE_IN_SAME_PLACE,
    }

    val validRiverCrosserTypes: Set<RiverCrosserType>

    /**
     * Define which crosser types can drive the boat.
     */
    val boatDriverTypes: Set<RiverCrosserType>

    /**
     * Define which crosser types can stay together in the same place and which crosser types will cause game over.
     * For the definition of same place, see `areBoatAndNearByRiversideInSamePlace`.
     * List is used instead of Set is because some rule depend on number of crossers of the same types. e.g. two daughters at same place without parents will fight together.
     */
    fun canGameContinue(crosserTypesInSamePlace: List<RiverCrosserType>): Boolean

    val samePlaceMode: SamePlaceMode

    val boatCapacity: Int
        get() = 2

    /**
     * If it is true, TRANSIT move can only move one crosser.
     */
    val transitOneCrosserOnly: Boolean
        get() = false
}

interface MoveTypeCostRules {
    fun getMoveCost(moveType: MoveType): Int
}