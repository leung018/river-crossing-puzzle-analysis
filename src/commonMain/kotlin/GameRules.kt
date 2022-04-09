import RiverCrosserPosition.*

data class RiverCrosserType(val id: String, val occupiedBoatSpace: Int = 1)
// Use data class instead of enum because for future feature if crosser type is defined by external config

val DOG = RiverCrosserType("DOG")
val FATHER = RiverCrosserType("FATHER")
val MOTHER = RiverCrosserType("MOTHER")
val SON = RiverCrosserType("SON")
val DAUGHTER = RiverCrosserType("DAUGHTER")
val MASTER = RiverCrosserType("MASTER")

object ClassicGameRules : GameRules {
    override val canDriveBoatCrosserTypes = setOf(FATHER, MOTHER)

    override fun canGameContinue(crosserTypesInSamePlace: Set<RiverCrosserType>): Boolean {
        val typesSet: Set<RiverCrosserType> = crosserTypesInSamePlace
        if (typesSet.contains(FATHER) && !typesSet.contains(MOTHER) && typesSet.contains(DAUGHTER))
            return false
        if (typesSet.contains(MOTHER) && !typesSet.contains(FATHER) && typesSet.contains(SON))
            return false
        if (typesSet.contains(DOG) && !typesSet.contains(MASTER) && typesSet.minus(DOG).isNotEmpty())
            return false
        return true
    }

    override val samePlaceDefinitions: Set<Set<RiverCrosserPosition>> = setOf(
        setOf(ORIGINAL_RIVER_SIDE),
        setOf(BOAT_ON_ORIGINAL_RIVER_SIZE),
        setOf(BOAT_ON_TARGET_RIVER_SIDE),
        setOf(TARGET_RIVER_SIDE)
    )

    override fun getMoveCost(type: MoveType): Int {
        return when (type) {
            MoveType.TRANSIT -> 0
            MoveType.DRIVE_BOAT -> 1
        }
    }
}

interface GameRules : GameSituationRules, MoveTypeCostRules

interface GameSituationRules {
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