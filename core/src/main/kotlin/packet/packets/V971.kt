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
 * 回忆之旅通关
 *
 * @property gl 关卡 id
 * @property r [TODO]
 * @property tgt [TODO]
 * @property tp [TODO]
 */
@PacketIdentifier("V971")
class V971(override val user: User) : Packet() {
    override val identifier: String = "V971"

    /**
     * 关卡 id
     *
     * ```
     * gl = "0"
     * ```
     */
    var gl: String = "0"

    /**
     * [TODO]
     *
     * ```
     * r = "0" //
     * r = "2" //
     * ```
     */
    var r: String = "0"

    /**
     * [TODO]
     *
     * ```
     * tgt = "0, 1, 2" //
     * tgt = "1, 2, 3" //
     * ```
     */
    var tgt: String = "0, 1, 2"

    /**
     * [TODO]
     *
     * ```
     * tp = "0" //
     * tp = "1" //
     * tp = "2" //
     * ```
     */
    var tp: String = "0"

    @Serializable
    data class Data(
        @SerialName("gl") val gl: String,
        @SerialName("pi") val pi: String,
        @SerialName("r") val r: String,
        @SerialName("sk") val sk: String,
        @SerialName("tgt") val tgt: String,
        @SerialName("tp") val tp: String,
        @SerialName("ui") val ui: String,
        @SerialName("wi") val wi: String = "1",
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            gl = gl,
            r = r,
            tgt = tgt,
            tp = tp,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

