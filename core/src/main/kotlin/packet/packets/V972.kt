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
 * 回忆之旅商店兑换刷新
 *
 * @property c [TODO]
 * @property s [TODO]
 */
@PacketIdentifier("V972")
class V972(override val user: User) : Packet() {
    override val identifier: String = "V972"

    /**
     * [TODO]
     *
     * ```
     * c = "0"
     * ```
     */
    var c: String = "0"

    /**
     * [TODO]
     *
     * ```
     * s = "0"
     * ```
     */
    var s: String = "0"

    @Serializable
    data class Data(
        @SerialName("c") val c: String,
        @SerialName("pi") val pi: String,
        @SerialName("s") val s: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            c = c,
            pi = user.credential.personalId,
            s = s,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

