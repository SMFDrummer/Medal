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
 * 潘妮课堂回答问题奖励
 *
 * @property i [TODO]
 * @property s [TODO]
 */
@PacketIdentifier("V760")
class V760(override val user: User) : Packet() {
    override val identifier: String = "V760"

    /**
     * [TODO]
     *
     * ```
     * i = "1"
     * ```
     */
    var i: String = "1"

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
        @SerialName("i") val i: String,
        @SerialName("pi") val pi: String,
        @SerialName("s") val s: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            i = i,
            pi = user.credential.personalId,
            s = s,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

