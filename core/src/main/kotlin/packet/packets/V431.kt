package packet.packets

import JsonFeature
import by
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import packet.Packet
import service.model.User
import primitive
import packet.PacketIdentifier
/**
 * 通行证奖励领取
 *
 * @property ai 等级奖励领取, 可以为负数（bug）
 * @property bai 选择第几个奖励
 */
@PacketIdentifier("V431")
class V431(override val user: User) : Packet() {
    override val identifier: String = "V431"

    /**
     * 等级奖励领取, 可以为负数（bug）
     *
     * ```
     * ai = -1
     * ai = "1"
     * ```
     */
    var ai: JsonPrimitive = primitive { "1" }

    /**
     * 选择第几个奖励, 默认为 0
     *
     * ```
     * bai = "0"
     * ```
     */
    var bai: String = "0"

    @Serializable
    data class Data(
        @SerialName("ai") val ai: JsonPrimitive,
        @SerialName("bai") val bai: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("type") val type: String = "0",
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            ai = ai,
            bai = bai,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

