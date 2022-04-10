package gamePlay

import rules.GameSituationRules
import rules.RiverCrosserType
import rules.classic.ClassicGameRules
import kotlin.test.Test
import kotlin.test.assertFailsWith

private fun newGameSituationTeller(
    crossers: List<RiverCrosser>,
    rules: GameSituationRules = ClassicGameRules
): GameSituationTeller {
    return GameSituationTeller(crossers, rules)
}

internal class GameSituationTellerTest {
    @Test
    fun `constructor when crossers list empty`() {
        assertFailsWith<IllegalArgumentException> {
            newGameSituationTeller(crossers = listOf())
        }
    }

    @Test
    fun `constructor when crossers list contain type that is not valid`() {
        val nonClassicType = RiverCrosserType("NOT_VALID")
        assertFailsWith<IllegalArgumentException> {
            newGameSituationTeller(crossers = listOf(RiverCrosser(nonClassicType)), ClassicGameRules)
        }
    }
}