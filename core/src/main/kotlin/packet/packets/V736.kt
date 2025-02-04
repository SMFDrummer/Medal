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
 * 创意庭院夸赞
 *
 * @property id 关卡号
 * @property tag 夸赞文本序号
 */
@PacketIdentifier("V736")
class V736(override val user: User) : Packet() {
    override val identifier: String = "V736"

    /**
     * 关卡号
     *
     * ```
     * id = "27703998"
     * ```
     */
    var id: String = "27703998"

    /**
     * 夸赞文本序号
     *
     * ```
     * tag = "1"
     * ```
     */
    var tag: String = "1"

    @Serializable
    data class Data(
        @SerialName("id") val id: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("tag") val tag: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            id = id,
            tag = tag,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

