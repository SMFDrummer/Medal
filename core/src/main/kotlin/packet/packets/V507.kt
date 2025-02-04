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
 * @property g [TODO]
 * @property t [TODO]
 */
@PacketIdentifier("V507")
class V507(override val user: User) : Packet() {
    override val identifier: String = "V507"

    /**
     * [TODO]
     *
     * ```
     * g = null //
     * g = "101" //
     * ```
     */
    var g: String? = null

    /**
     * [TODO]
     *
     * ```
     * t = null //
     * t = "1001, 1003, 1009" //
     * ```
     */
    var t: String? = null

    @Serializable
    data class Data(
        @SerialName("g") val g: String?,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("t") val t: String?,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            g = g,
            t = t,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

