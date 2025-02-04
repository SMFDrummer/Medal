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
 * @property l [TODO]
 * @property s [TODO]
 * @property w [TODO]
 */
@PacketIdentifier("V1091")
class V1091(override val user: User) : Packet() {
    override val identifier: String = "V1091"

    /**
     * [TODO]
     *
     * ```
     * l = [TODO]
     * ```
     */
    var l: String = ""

    /**
     * [TODO]
     *
     * ```
     * s = [TODO]
     * ```
     */
    var s: String = ""

    /**
     * [TODO]
     *
     * ```
     * w = [TODO]
     * ```
     */
    var w: String = ""

    @Serializable
    data class Data(
        @SerialName("l") val l: String,
        @SerialName("pi") val pi: String,
        @SerialName("s") val s: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
        @SerialName("w") val w: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            l = l,
            pi = user.credential.personalId,
            s = s,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
            w = w
        )
    ).jsonObject
}