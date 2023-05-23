package game.rules


data class RiverCrosserType(
    val id: String,
    val occupiedBoatCapacity: Int = 1
)
// Use data class instead of enum because for future feature if crosser type is defined by external config

interface GameRules : GameSituationRules, MoveTypeCostRules

interface GameSituationRules {
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

    /**
     * If it is true, it means that the boat and the nearby riverside are in the same place.
     *
     * e.g. Given that crosser A on the boat and crosser B on the nearby riverside.
     * A and B are considered in the same place if and only if `areBoatAndNearByRiversideInSamePlace` is true.
     */
    val areBoatAndNearByRiversideInSamePlace: Boolean

    val boatCapacity: Int
        get() = 2
}

interface MoveTypeCostRules {
    fun getMoveCost(moveType: MoveType): Int
}