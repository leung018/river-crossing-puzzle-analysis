data class Move(val moveType: MoveType, val targetPosition: RiverCrosserPosition)

enum class MoveType {
    DRIVE_BOAT,
    TRANSIT
}
