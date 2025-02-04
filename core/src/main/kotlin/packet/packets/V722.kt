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
 * 创意庭院关卡评价
 *
 * @property id 关卡号
 * @property t 评价关卡
 */
@PacketIdentifier("V722")
class V722(override val user: User) : Packet() {
    override val identifier: String = "V722"

    /**
     * 关卡号
     *
     * ```
     * id = "27703998"
     * ```
     */
    var id: String = "27703998"

    /**
     * 评价关卡
     *
     * ```
     * t = "1" // 点赞关卡
     * t = "2" // 踩关卡
     * ```
     */
    var t: String = "1"

    @Serializable
    data class Data(
        @SerialName("id") val id: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("t") val t: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            id = id,
            t = t,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

