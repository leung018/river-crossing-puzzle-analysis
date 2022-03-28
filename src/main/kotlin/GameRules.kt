data class RiverCrosserType(val id: String)
// Use data class instead of enum because for future feature if crosser type is defined by external config

val DOG = RiverCrosserType("DOG")
val FATHER = RiverCrosserType("FATHER")
val MOTHER = RiverCrosserType("MOTHER")
val SON = RiverCrosserType("SON")
val DAUGHTER = RiverCrosserType("DAUGHTER")
val MASTER = RiverCrosserType("MASTER")

object GameRules {
    val CAN_DRIVE_BOAT = setOf(FATHER, MOTHER)

    fun canGameContinue(crosserTypesInSamePosition: Set<RiverCrosserType>): Boolean {
        val typesSet: Set<RiverCrosserType> = crosserTypesInSamePosition
        if (typesSet.contains(FATHER) && !typesSet.contains(MOTHER) && typesSet.contains(DAUGHTER))
            return false
        if (typesSet.contains(MOTHER) && !typesSet.contains(FATHER) && typesSet.contains(SON))
            return false
        if (typesSet.contains(DOG) && !typesSet.contains(MASTER) && typesSet.minus(DOG).isNotEmpty())
            return false
        return true
    }
}