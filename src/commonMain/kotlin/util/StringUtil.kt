package util

fun String.trimAllLines(): String {
    return this.lines().joinToString("\n") { it.trim() }
}