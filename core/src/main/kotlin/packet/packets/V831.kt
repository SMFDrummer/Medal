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
 * 双人对决段位
 *
 * @property index [TODO]
 */
@PacketIdentifier("V831")
class V831(override val user: User) : Packet() {
    override val identifier: String = "V831"

    /**
     * [TODO]
     *
     * ```
     * index = "4"
     * ```
     */
    var index: String = "4"

    @Serializable
    data class Data(
        @SerialName("index") val index: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            index = index,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

