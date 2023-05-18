package game.rules

enum class MoveType {
    /**
     *  Drive boat to the other riverside.
     */
    DRIVE_BOAT,

    /**
     * Move from riverside to boat or vice versa.
     */
    TRANSIT
}