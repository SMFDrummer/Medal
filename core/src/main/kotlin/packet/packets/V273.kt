package packet.packets

import packet.PacketIdentifier
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

/**
 * 挑战僵博
 *
 * @property bi [TODO]
 * @property id [TODO]
 * @property t 挑战时间
 * @property l [TODO]
 * @property k [TODO]
 */
@PacketIdentifier("V273")
class V273(override val user: User) : Packet() {
    override val identifier: String = "V273"

    /**
     * [TODO]
     *
     * ```
     * bi = "0"
     * ```
     */
    var bi: String = "0"

    /**
     * [TODO]
     *
     * ```
     * id = 0
     * ```
     */
    var id: Int = 0

    /**
     * 挑战时间
     *
     * ```
     * t = 34.567890
     * ```
     */
    var t: Double = 34.567890

    /**
     * [TODO]
     *
     * ```
     * l = 0.0
     * ```
     */
    var l: Double = 0.0

    /**
     * [TODO]
     *
     * ```
     * k = 0
     * ```
     */
    var k: Int = 0

    @Serializable
    data class Data(
        @SerialName("bi") val bi: String,
        @SerialName("d") val d: List<D>,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    ) {
        @Serializable
        data class D(
            @SerialName("id") val id: Int,
            @SerialName("k") val k: Int,
            @SerialName("l") val l: Double,
            @SerialName("t") val t: Double,
        )
    }

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            bi = bi,
            d = listOf(
                Data.D(
                    id = id,
                    k = k,
                    l = l,
                    t = t
                )
            ),
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

