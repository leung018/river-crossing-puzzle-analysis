package testUtil

import gamePlay.GamePlayPositions
import gamePlay.GameSituationTeller
import gamePlay.RiverCrosser
import rules.GameSituationRules
import rules.RiverCrosserPosition
import rules.classic.ClassicGameRules
import rules.classic.FATHER
import rules.classic.MASTER

fun newGameSituationTeller(
    gameplayPositions: GamePlayPositions,
    rules: GameSituationRules = ClassicGameRules,
): GameSituationTeller {
    return GameSituationTeller(gameplayPositions, rules)
}

fun newClassicCrosser(position: RiverCrosserPosition, canDriveBoat: Boolean = false) =
    RiverCrosser(type = if (canDriveBoat) FATHER else MASTER, position)