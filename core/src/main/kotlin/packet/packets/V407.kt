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
 * 兑换齿轮
 *
 * @property key 兑换齿轮
 */
@PacketIdentifier("V407")
class V407(override val user: User) : Packet() {
    override val identifier: String = "V407"

    /**
     * 兑换齿轮
     *
     * ```
     * key = "4"
     * ```
     */
    var key: String = "4"

    @Serializable
    data class Data(
        @SerialName("key") val key: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            key = key,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

