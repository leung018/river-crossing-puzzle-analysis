package testutil

import game.rules.RiverCrosserType
import game.rules.classic.FATHER
import game.rules.classic.SON

fun newClassicCrosserType(canDriveBoat: Boolean = false): RiverCrosserType =
    if (canDriveBoat) FATHER else SON