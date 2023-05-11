package rules

enum class BoatPosition {
    ORIGINAL_RIVERSIDE, TARGET_RIVERSIDE
}

fun BoatPosition.opposite(): BoatPosition =
    when (this) {
        BoatPosition.ORIGINAL_RIVERSIDE -> BoatPosition.TARGET_RIVERSIDE
        BoatPosition.TARGET_RIVERSIDE -> BoatPosition.ORIGINAL_RIVERSIDE
    }

fun BoatPosition.nearbyRiversideForCrosser(): RiverCrosserPosition =
    when (this) {
        BoatPosition.ORIGINAL_RIVERSIDE -> RiverCrosserPosition.ORIGINAL_RIVERSIDE
        BoatPosition.TARGET_RIVERSIDE -> RiverCrosserPosition.TARGET_RIVERSIDE
    }

enum class RiverCrosserPosition {
    ORIGINAL_RIVERSIDE, BOAT, TARGET_RIVERSIDE
}