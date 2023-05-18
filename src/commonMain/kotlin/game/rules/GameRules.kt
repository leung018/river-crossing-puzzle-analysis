package game.rules


data class RiverCrosserType(
    val id: String,
    val occupiedBoatSpace: Int = 1
) // TODO: use occupiedBoatSpace instead of counting number of crossers in GameSituationTeller
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
     */
    fun canGameContinue(crosserTypesInSamePlace: Set<RiverCrosserType>): Boolean

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