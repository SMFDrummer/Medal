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
 * @property n [TODO]
 * @property s [TODO]
 * @property t [TODO]
 */
@PacketIdentifier("V902")
class V902(override val user: User) : Packet() {
    override val identifier: String = "V902"

    /**
     * [TODO]
     *
     * ```
     * n = "20"
     * ```
     */
    var n: String = "20"

    /**
     * [TODO]
     *
     * ```
     * s = "1"
     * ```
     */
    var s: String = "1"

    /**
     * [TODO]
     *
     * ```
     * t = "4"
     * ```
     */
    var t: String = "4"


    @Serializable
    data class Data(
        @SerialName("n") val n: String,
        @SerialName("pi") val pi: String,
        @SerialName("s") val s: String,
        @SerialName("sk") val sk: String,
        @SerialName("t") val t: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            n = n,
            s = s,
            t = t,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

