data class Move(val moveType: MoveType, val targetPosition: RiverCrosserPosition)

enum class MoveType {
    DRIVE_BOAT {
        override val cost: Int = 1
    },
    TRANSIT {
        override val cost: Int = 0
    };

    abstract val cost: Int
}

