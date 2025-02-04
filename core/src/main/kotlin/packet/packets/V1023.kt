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
 * 未来礼盒奖励
 *
 * @property i 未来礼盒个人累计进度奖励
 * @property t 未来礼盒奖励类型
 */
@PacketIdentifier("V1023")
class V1023(override val user: User) : Packet() {
    override val identifier: String = "V1023"

    /**
     * 未来礼盒个人累计进度奖励
     *
     * ```
     * i = "0" // 0 ~ 6
     * ```
     */
    var i: String = "0"

    /**
     * 未来礼盒奖励类型
     *
     * ```
     * t = "0" // 全服奖励
     * t = "1" // 个人奖励
     * ```
     */
    var t: String = "0"

    @Serializable
    data class Data(
        @SerialName("i") val i: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("t") val t: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            i = i,
            t = t,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}


