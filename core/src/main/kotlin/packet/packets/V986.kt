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
 * 时空寻宝奖励
 *
 * @property t 奖励状态
 */
@PacketIdentifier("V986")
class V986(override val user: User) : Packet() {
    override val identifier: String = "V986"

    /**
     * 奖励状态
     *
     * ```
     * t = "0" // 领取奖励
     * t = "1" // 刷新奖励
     * ```
     */
    var t: String = "0"

    @Serializable
    data class Data(
        @SerialName("ai") val ai: String = "0",
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
        )
    ).jsonObject
}

