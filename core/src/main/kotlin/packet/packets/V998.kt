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
 * 新人 7 天签到
 */
@PacketIdentifier("V998")
class V998(override val user: User) : Packet() {
    override val identifier: String = "V998"

    @Serializable
    data class Data(
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}