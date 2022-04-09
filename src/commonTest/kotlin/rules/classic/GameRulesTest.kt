package rules.classic

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ClassicGameRulesTest {

    @Test
    fun `ClassicGameRules canGameContinue`() {
        assertTrue(ClassicGameRules.canGameContinue(setOf()))

        assertFalse(
            ClassicGameRules.canGameContinue(setOf(FATHER, DAUGHTER)),
            "father will beat daughter without mother"
        )
        assertTrue(
            ClassicGameRules.canGameContinue(setOf(FATHER, DAUGHTER, MOTHER)),
            "mother should prevent father beat daughter"
        )

        assertFalse(ClassicGameRules.canGameContinue(setOf(MOTHER, SON)), "mother will beat son without father")
        assertTrue(
            ClassicGameRules.canGameContinue(setOf(MOTHER, SON, FATHER)),
            "father should prevent mother beat son"
        )

        assertFalse(ClassicGameRules.canGameContinue(setOf(DOG, FATHER)), "dog will bite others without master")
        assertTrue(ClassicGameRules.canGameContinue(setOf(DOG)), "dog cannot bite no one")
        assertTrue(ClassicGameRules.canGameContinue(setOf(DOG, DOG)), "dog cannot bite no one")
        assertTrue(
            ClassicGameRules.canGameContinue(setOf(DOG, FATHER, MASTER)),
            "master should prevent dog biting others"
        )

        assertTrue(ClassicGameRules.canGameContinue(setOf(DOG, MASTER, FATHER, MOTHER, SON, DAUGHTER, DAUGHTER)))
    }
}