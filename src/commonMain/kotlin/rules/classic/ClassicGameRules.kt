package rules.classic

import rules.GameRules
import rules.Move
import rules.RiverCrosserType

val DOG = RiverCrosserType("DOG")
val FATHER = RiverCrosserType("FATHER")
val MOTHER = RiverCrosserType("MOTHER")
val SON = RiverCrosserType("SON")
val DAUGHTER = RiverCrosserType("DAUGHTER")
val MASTER = RiverCrosserType("MASTER")

object ClassicGameRules : GameRules {
    override val validRiverCrosserTypes: Set<RiverCrosserType> = setOf(DOG, FATHER, MOTHER, SON, DAUGHTER, MASTER)
    override val boatDriverTypes = setOf(FATHER, MOTHER)

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

    override fun getMoveCost(move: Move): Int {
        return when (move) {
            Move.TRANSIT -> 0
            Move.DRIVE_BOAT -> 1
        }
    }

    override val areBoatAndNearByRiversideInSamePlace: Boolean = false
}