import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class GameRulesTest {

    @Test
    fun canGameContinue() {
        assertTrue(GameRulesObj.canGameContinue(setOf()))

        assertFalse(GameRulesObj.canGameContinue(setOf(FATHER, DAUGHTER)), "father will beat daughter without mother")
        assertTrue(GameRulesObj.canGameContinue(setOf(FATHER, DAUGHTER, MOTHER)), "mother prevent father beat daughter")

        assertFalse(GameRulesObj.canGameContinue(setOf(MOTHER, SON)), "mother will beat son without father")
        assertTrue(GameRulesObj.canGameContinue(setOf(MOTHER, SON, FATHER)), "father prevent mother beat son")

        assertFalse(GameRulesObj.canGameContinue(setOf(DOG, FATHER)), "dog will bite others without master")
        assertTrue(GameRulesObj.canGameContinue(setOf(DOG)), "dog cannot bite no one")
        assertTrue(GameRulesObj.canGameContinue(setOf(DOG, DOG)), "dog cannot bite no one")
        assertTrue(GameRulesObj.canGameContinue(setOf(DOG, FATHER, MASTER)), "master prevent dog biting others")

        assertTrue(GameRulesObj.canGameContinue(setOf(DOG, MASTER, FATHER, MOTHER, SON, DAUGHTER, DAUGHTER)))
    }
}