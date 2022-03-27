import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import RiverCrosserType.*

internal class GameRulesTest {

    @Test
    fun canGameContinue() {
        assertTrue(GameRules.canGameContinue(setOf()))

        assertFalse(GameRules.canGameContinue(setOf(FATHER, DAUGHTER)), "father will beat daughter without mother")
        assertTrue(GameRules.canGameContinue(setOf(FATHER, DAUGHTER, MOTHER)), "mother prevent father beat daughter")

        assertFalse(GameRules.canGameContinue(setOf(MOTHER, SON)), "mother will beat son without father")
        assertTrue(GameRules.canGameContinue(setOf(MOTHER, SON, FATHER)), "father prevent mother beat son")

        assertFalse(GameRules.canGameContinue(setOf(DOG, FATHER)), "dog will bite others without master")
        assertTrue(GameRules.canGameContinue(setOf(DOG)), "dog cannot bite no one")
        assertTrue(GameRules.canGameContinue(setOf(DOG, DOG)), "dog cannot bite no one")
        assertTrue(GameRules.canGameContinue(setOf(DOG, FATHER, MASTER)), "master prevent dog biting others")

        assertTrue(GameRules.canGameContinue(setOf(DOG, MASTER, FATHER, MOTHER, SON, DAUGHTER, DAUGHTER)))
    }
}