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
 * 扭蛋抽奖
 *
 * @property on 抽奖次数
 */
@PacketIdentifier("V348")
class V348(override val user: User) : Packet() {
    override val identifier: String = "V348"

    /**
     * 抽奖次数, 默认为 10
     *
     * ```
     * on = "10" // 十连抽
     * ```
     */
    var on: String = "10"

    @Serializable
    data class Data(
        @SerialName("ai") val ai: String = "10759", // 活动 id
        @SerialName("on") val on: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            on = on,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

