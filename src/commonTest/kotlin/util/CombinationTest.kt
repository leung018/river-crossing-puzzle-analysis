package util

import kotlin.test.Test
import kotlin.test.assertEquals

class CombinationTest {
    @Test
    fun `test getCombinations with maxNumOfElements equal to number of input set elements`() {
        val inputSet = setOf(1, 2, 3)
        val maxNumOfElements = 3
        val expectedCombinations = setOf(
            setOf(1),
            setOf(2),
            setOf(3),
            setOf(1, 2),
            setOf(1, 3),
            setOf(2, 3),
            setOf(1, 2, 3)
        )

        val actualCombinations = getCombinations(inputSet, maxNumOfElements)

        assertEquals(expectedCombinations, actualCombinations)
    }

    @Test
    fun `test getCombinations with 0 maxNumOfElements`() {
        val inputSet = setOf(1, 2, 3)
        val maxNumOfElements = 0
        val expectedCombinations = emptySet<Set<Int>>()

        val actualCombinations = getCombinations(inputSet, maxNumOfElements)

        assertEquals(expectedCombinations, actualCombinations)
    }

    @Test
    fun `test getCombinations with maxNumOfElements larger than number of input set elements`() {
        val inputSet = setOf(1, 2, 3)
        val maxNumOfElements = 5
        val expectedCombinations = setOf(
            setOf(1),
            setOf(2),
            setOf(3),
            setOf(1, 2),
            setOf(1, 3),
            setOf(2, 3),
            setOf(1, 2, 3)
        )

        val actualCombinations = getCombinations(inputSet, maxNumOfElements)

        assertEquals(expectedCombinations, actualCombinations)
    }
}