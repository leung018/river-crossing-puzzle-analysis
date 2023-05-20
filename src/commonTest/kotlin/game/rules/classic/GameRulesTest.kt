package game.rules.classic

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ClassicGameRulesTest {

    @Test
    fun `ClassicGameRules canGameContinue`() {
        assertTrue(ClassicGameRules.canGameContinue(listOf()))

        assertFalse(
            ClassicGameRules.canGameContinue(listOf(FATHER, DAUGHTER)),
            "father will beat daughter without mother"
        )
        assertTrue(
            ClassicGameRules.canGameContinue(listOf(FATHER, DAUGHTER, MOTHER)),
            "mother should prevent father beat daughter"
        )

        assertFalse(ClassicGameRules.canGameContinue(listOf(MOTHER, SON)), "mother will beat son without father")
        assertTrue(
            ClassicGameRules.canGameContinue(listOf(MOTHER, SON, FATHER)),
            "father should prevent mother beat son"
        )

        assertFalse(ClassicGameRules.canGameContinue(listOf(DOG, FATHER)), "dog will bite others without master")
        assertTrue(ClassicGameRules.canGameContinue(listOf(DOG)), "dog cannot bite no one")
        assertTrue(ClassicGameRules.canGameContinue(listOf(DOG, DOG)), "dog cannot bite no one")
        assertTrue(
            ClassicGameRules.canGameContinue(listOf(DOG, FATHER, MASTER)),
            "master should prevent dog biting others"
        )

        assertTrue(ClassicGameRules.canGameContinue(listOf(DOG, MASTER, FATHER, MOTHER, SON, DAUGHTER, DAUGHTER)))
    }
}