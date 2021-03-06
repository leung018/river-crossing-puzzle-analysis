package rules


data class RiverCrosserType(val id: String, val occupiedBoatSpace: Int = 1)
// Use data class instead of enum because for future feature if crosser type is defined by external config

interface GameRules : GameSituationRules, MoveTypeCostRules

interface GameSituationRules {
    val validRiverCrosserTypes: Set<RiverCrosserType>
    val canDriveBoatCrosserTypes: Set<RiverCrosserType>
    fun canGameContinue(crosserTypesInSamePlace: Set<RiverCrosserType>): Boolean

    /**
     * Define which set of positions are considered as the same place
     */
    val samePlaceDefinitions: Set<Set<RiverCrosserPosition>>
    val boatCapacity: Int
        get() = 2
}

interface MoveTypeCostRules {
    fun getMoveCost(type: MoveType): Int
}