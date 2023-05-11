package util

/** @return all combinations of elements in the input set which have size less than or equal to `maxNumOfElements`
 */
fun <T> getCombinations(inputSet: Set<T>, maxNumOfElements: Int): Set<Set<T>> {
    val combinations = mutableSetOf<Set<T>>()
    for (i in 1..maxNumOfElements) {
        combinations.addAll(getCombinationsOfLength(inputSet, i))
    }
    return combinations
}

private fun <T> getCombinationsOfLength(inputSet: Set<T>, length: Int): Set<Set<T>> {
    if (length == 0) {
        return setOf(setOf())
    }
    if (length == inputSet.size) {
        return setOf(inputSet)
    }
    if (length > inputSet.size) {
        return setOf()
    }

    val combinations = mutableSetOf<Set<T>>()

    for (item in inputSet) {
        val smallerSet = inputSet.filter { it != item }.toSet()
        val smallerCombinations = getCombinationsOfLength(smallerSet, length - 1)
        for (combination in smallerCombinations) {
            combinations.add(combination + item)
        }
    }

    return combinations
}