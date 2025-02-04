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
 * 金钞票报名包
 *
 * @property id 报名状态
 */
@PacketIdentifier("V1122")
class V1122(override val user: User) : Packet() {
    override val identifier: String = "V1122"

    /**
     * 报名状态
     *
     * ```
     * id = 1001
     * id = 1002
     * id = 1003
     * ```
     */
    var id: String = "1001"

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