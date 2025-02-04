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
 * 狂欢嘉年华进度奖励
 *
 * @property t 奖励状态
 */
@PacketIdentifier("V816")
class V816(override val user: User) : Packet() {
    override val identifier: String = "V816"

    /**
     * 奖励状态
     *
     * ```
     * t = "1" // 个人进度奖励
     * t = "2" // 全服进度奖励
     * ```
     */
    var t: String = "1"

    @Serializable
    data class Data(
        @SerialName("index") val index: String = "0",
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

