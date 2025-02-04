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
 * 植物培育选择植物
 *
 * @property plantId 植物 id
 */
@PacketIdentifier("V987")
class V987(override val user: User) : Packet() {
    override val identifier: String = "V987"

    /**
     * 植物 id, 默认为 200086
     *
     * ```
     * plantId = "200086“
     * ```
     */
    var plantId: String = "200086"

    @Serializable
    data class Data(
        @SerialName("pi") val pi: String,
        @SerialName("plant_id") val plantId: String,
        @SerialName("sk") val sk: String,
        @SerialName("t") val t: String = "0",
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            plantId = plantId,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

