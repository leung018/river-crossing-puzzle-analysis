package game.rules.classic

import game.rules.GameRules
import game.rules.GameSituationRules
import game.rules.MoveType
import game.rules.RiverCrosserType

val DOG = RiverCrosserType("DOG")
val FATHER = RiverCrosserType("FATHER")
val MOTHER = RiverCrosserType("MOTHER")
val SON = RiverCrosserType("SON")
val DAUGHTER = RiverCrosserType("DAUGHTER")
val MASTER = RiverCrosserType("MASTER")

object ClassicGameRules : GameRules {
    override val validRiverCrosserTypes: Set<RiverCrosserType> = setOf(DOG, FATHER, MOTHER, SON, DAUGHTER, MASTER)
    override val boatDriverTypes = setOf(FATHER, MOTHER, MASTER)

    override fun canGameContinue(crosserTypesInSamePlace: List<RiverCrosserType>): Boolean {
        val types: List<RiverCrosserType> = crosserTypesInSamePlace
        if (types.contains(FATHER) && !types.contains(MOTHER) && types.contains(DAUGHTER))
            return false
        if (types.contains(MOTHER) && !types.contains(FATHER) && types.contains(SON))
            return false
        if (types.contains(DOG) && !types.contains(MASTER) && types.any { it != DOG })
            return false
        return true
    }

    override fun getMoveCost(moveType: MoveType): Int {
        return when (moveType) {
            MoveType.TRANSIT -> 0
            MoveType.DRIVE_BOAT -> 1
        }
    }

    override val samePlaceMode: GameSituationRules.SamePlaceMode =
        GameSituationRules.SamePlaceMode.BOAT_AND_RIVERSIDE_IN_DIFFERENT_PLACE
}