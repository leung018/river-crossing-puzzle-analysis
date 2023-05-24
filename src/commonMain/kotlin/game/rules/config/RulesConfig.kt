package game.rules.config

import game.rules.GameRules
import game.rules.GameSituationRules
import kotlinx.serialization.Serializable

@Serializable
data class RulesConfig(
    val validRiverCrosserTypes: List<RiverCrosserTypeConfig>,
    val boatCapacity: Int = 2,
    val initCrosserTypes: List<String>,
    val boatDriverTypes: List<String>,
    val samePlaceMode: GameSituationRules.SamePlaceMode = GameSituationRules.SamePlaceMode.BOAT_AND_NEARBY_RIVERSIDE_IN_SAME_PLACE,
    val transitOneCrosserOnly: Boolean = true,
    val moveCostConfig: MoveCostConfig = MoveCostConfig(),
    val gameOverConfigs: List<GameOverConfig>
) {
    fun getGameRules(): GameRules {
        TODO()
    }
}

@Serializable
data class RiverCrosserTypeConfig(
    val id: String,
    val capacity: Int = 1
)

@Serializable
data class MoveCostConfig(
    val transit: Int = 0,
    val driveBoat: Int = 1
)

@Serializable
data class GameOverConfig(
    val conflictCrosserTypes: List<String>,
    val preventConflictCrosserTypes: List<String>,
)