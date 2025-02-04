package packet.packets

import JsonFeature
import by
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import packet.Packet
import service.model.User
import packet.PacketIdentifier
/**
 * 限时召唤抽奖及累计召唤
 *
 * @property t 请求状态
 */
@PacketIdentifier("V811")
class V811(override val user: User) : Packet() {
    override val identifier: String = "V811"

    /**
     * 请求状态
     *
     * ```
     * t = "1" // 累计召唤
     * t = "2 // 10 连抽
     * ```
     */
    var t: String = "1"

    var ci: String? = if (t == "1") "0" else null

    @Serializable
    data class Data(
        @SerialName("ai") val ai: String = "10854", // 活动 id
        @SerialName("ci") val ci: String?,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("t") val t: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            t = t,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
            ci = ci
        )
    ).jsonObject
}

