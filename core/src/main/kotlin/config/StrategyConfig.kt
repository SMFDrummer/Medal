package config

import by
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

@Serializable
data class StrategyConfig(
    val version: Int = 1,
    val description: String = "null",
    val packets: List<PacketConfig>
) {
    @Serializable
    data class PacketConfig(
        val i: String,
        val r: Int,
        val t: MutableMap<String, JsonElement> = linkedMapOf(),
        val cycle: Int = 1,
        val retry: Int = 0,
        val onSuccess: JsonPrimitive? = null,
        val onFailure: JsonPrimitive? = null,
    )

    override fun toString(): String = "版本：${version} -> 描述：${description}"

    fun toJsonString(): String = Json.by(
        JsonFeature.ImplicitNulls,
        JsonFeature.EncodeDefaults,
        JsonFeature.PrettyPrint
    ).encodeToString(this)
}
