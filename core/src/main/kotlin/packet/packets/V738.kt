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
 * 更换背景板
 *
 * @property bgid 背景板 id
 */
@PacketIdentifier("V738")
class V738(override val user: User) : Packet() {
    override val identifier: String = "V738"

    /**
     * 背景板 id
     *
     * ```
     * bgid = "61001"
     * ```
     */
    var bgid: String = "61001"

    @Serializable
    data class Data(
        @SerialName("bgid") val bgid: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            bgid = bgid,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

