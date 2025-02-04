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
 * @property id [TODO]
 * @property score [TODO]
 * @property type [TODO]
 */
@PacketIdentifier("V1070")
class V1070(override val user: User) : Packet() {
    override val identifier: String = "V1070"

    /**
     * [TODO]
     *
     * ```
     * id = [TODO]
     * ```
     */
    var id: String = ""

    /**
     * [TODO]
     *
     * ```
     * score = [TODO]
     * ```
     */
    var score: String = ""

    /**
     * [TODO]
     *
     * ```
     * type = [TODO]
     * ```
     */
    var type: String = ""

    @Serializable
    data class Data(
        @SerialName("id") val id: String,
        @SerialName("pi") val pi: String,
        @SerialName("score") val score: String,
        @SerialName("sk") val sk: String,
        @SerialName("type") val type: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            id = id,
            pi = user.credential.personalId,
            score = score,
            sk = user.credential.securityKey,
            type = type,
            ui = user.credential.userId,
        )
    ).jsonObject
}