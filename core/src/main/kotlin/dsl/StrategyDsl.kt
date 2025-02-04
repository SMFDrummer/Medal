package dsl

import config.StrategyConfig
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

class StrategyBuilder internal constructor() {
    var version: Int = 1
    var description: String = "null"
    val packets = mutableListOf<StrategyConfig.PacketConfig>()

    fun packet(
        i: String,
        r: Int = 0,
        t: MutableMap<String, JsonElement> = linkedMapOf(),
        cycle: Int = 1,
        retry: Int = 0,
        onSuccess: JsonPrimitive? = null,
        onFailure: JsonPrimitive? = null
    ) {
        packets.add(StrategyConfig.PacketConfig(i, r, t, cycle, retry, onSuccess, onFailure))
    }
}

fun buildStrategy(buildAction: StrategyBuilder.() -> Unit): StrategyConfig {
    val builder = StrategyBuilder().apply(buildAction)
    return StrategyConfig(builder.version, builder.description, builder.packets)
}