package util

import kotlinx.serialization.DeserializationStrategy
import net.mamoe.yamlkt.Yaml

fun <T> parseYamlStr(deserializer: DeserializationStrategy<T>, yamlStr: String): T {
    return Yaml.decodeFromString(
        deserializer,
        yamlStr,
    )
}