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
 * 旅行原木开宝箱
 *
 * @property bt 任务宝箱类别
 * @property ct 宝箱序号
 */
@PacketIdentifier("V791")
class V791(override val user: User) : Packet() {
    override val identifier: String = "V791"

    /**
     * 任务宝箱类别
     *
     * ```
     * bt = "0" // 小任务宝箱
     * // 其他宝箱种类代号未知
     * ```
     */
    var bt: String = "0"

    /**
     * 宝箱序号
     *
     * ```
     * ct = "1"
     * ```
     */
    var ct: String = "1"

    @Serializable
    data class Data(
        @SerialName("bt") val bt: String,
        @SerialName("ct") val ct: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            bt = bt,
            ct = ct,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

