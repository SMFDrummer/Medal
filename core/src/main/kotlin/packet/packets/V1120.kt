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
 * [TODO]
 *
 * @property type [TODO]
 */
@PacketIdentifier("V1120")
class V1120(override val user: User) : Packet() {
    override val identifier: String = "V1120"

    /**
     * [TODO]
     *
     * ```
     * type = ""
     * ```
     */
    var type: String = ""

    @Serializable
    data class Data(
        @SerialName("type") val type: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            type = type,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}