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
 * 兑换未来礼盒
 *
 * @property id 兑换未来礼盒奖励
 */
@PacketIdentifier("V1020")
class V1020(override val user: User) : Packet() {
    override val identifier: String = "V1020"

    /**
     * 兑换未来礼盒奖励
     *
     * ```
     * id = "0" // 0 ~ 7
     * ```
     */
    var id: String = "0"

    @Serializable
    data class Data(
        @SerialName("id") val id: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            id = id,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

