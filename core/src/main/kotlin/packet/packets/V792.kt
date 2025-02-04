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
 * 领取指定活动累计奖励或免费奖励
 *
 * @property ai 活动 id
 */
@PacketIdentifier("V792")
class V792(override val user: User) : Packet() {
    override val identifier: String = "V792"

    /**
     * 活动 id
     *
     * ```
     * ai = "10749" // 宝藏线索
     * ai = "10802" // 神秘商店
     * ai = "10803" // 追击指南
     * ```
     */
    var ai: String = ""

    @Serializable
    data class Data(
        @SerialName("ai") val ai: String,
        @SerialName("i") val i: String = "0",
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("t") val t: String = "1",
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            ai = ai,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

