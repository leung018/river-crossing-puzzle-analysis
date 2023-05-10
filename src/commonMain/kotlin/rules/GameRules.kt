package rules


data class RiverCrosserType(val id: String, val occupiedBoatSpace: Int = 1)
// Use data class instead of enum because for future feature if crosser type is defined by external config

interface GameRules : GameSituationRules, MoveTypeCostRules

interface GameSituationRules {
    val validRiverCrosserTypes: Set<RiverCrosserType>

    /**
     * Define which crosser types can drive the boat
     */
    val boatDriverTypes: Set<RiverCrosserType>

    fun canGameContinue(crosserTypesInSamePlace: Set<RiverCrosserType>): Boolean

    /**
     * Define which set of positions are considered as the same place
     */
    fun samePlaceDefinitions() {} // TODO
    val boatCapacity: Int
        get() = 2
}

interface MoveTypeCostRules {
    fun getMoveCost(move: Move): Int
}