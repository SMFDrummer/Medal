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
 * 创意庭院关卡收藏
 *
 * @property id 关卡号
 * @property op 收藏选项
 */
@PacketIdentifier("V728")
class V728(override val user: User) : Packet() {
    override val identifier: String = "V728"

    /**
     * 关卡号
     *
     * ```
     * id = "27703998"
     * ```
     */
    var id: String = "27703998"

    /**
     * 收藏选项, 默认为 1, 取消收藏可能为 0 或 2
     *
     * ```
     * op = "1"
     * ```
     */
    var op: String = "1"

    @Serializable
    data class Data(
        @SerialName("id") val id: String,
        @SerialName("op") val op: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            id = id,
            op = op,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

