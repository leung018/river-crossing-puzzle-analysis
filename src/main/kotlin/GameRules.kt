enum class RiverCrosserType {
    DOG, FATHER, MOTHER, SON, DAUGHTER
}

object GameRules {
    val CAN_DRIVE_BOAT = setOf(RiverCrosserType.FATHER, RiverCrosserType.MOTHER)

    fun canGameContinue(crossersInSamePosition: Set<RiverCrosser>): Boolean {
        TODO()
    }
}