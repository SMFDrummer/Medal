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
 * 追击通关选取加成
 *
 * @property i [TODO]
 * @property l [TODO]
 */
@PacketIdentifier("V929")
class V929(override val user: User) : Packet() {
    override val identifier: String = "V929"

    /**
     * [TODO]
     *
     * ```
     * i = "0"
     * ```
     */
    var i: String = "0"

    /**
     * [TODO]
     *
     * ```
     * l = "2"
     * ```
     */
    var l: String = "2"

    @Serializable
    data class Data(
        @SerialName("i") val i: String,
        @SerialName("l") val l: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            i = i,
            l = l,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

