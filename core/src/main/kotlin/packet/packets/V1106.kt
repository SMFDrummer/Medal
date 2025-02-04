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
 * @property q [TODO]
 */
@PacketIdentifier("V1106")
class V1106(override val user: User) : Packet() {
    override val identifier: String = "V1106"

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
     * q = [TODO]
     * ```
     */
    var q: String = ""

    @Serializable
    data class Data(
        @SerialName("bk") val bk: String = "1",
        @SerialName("id") val id: String,
        @SerialName("pi") val pi: String,
        @SerialName("q") val q: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            id = id,
            pi = user.credential.personalId,
            q = q,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}