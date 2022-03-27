import RiverCrosserType.*

enum class RiverCrosserType {
    DOG, FATHER, MOTHER, SON, DAUGHTER, MASTER
}

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