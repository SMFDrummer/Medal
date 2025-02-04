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
 * @property ad 广告状态
 * @property l [TODO]
 */
@PacketIdentifier("V323")
class V323(override val user: User) : Packet() {
    override val identifier: String = "V323"

    /**
     * 广告状态
     *
     * ```
     * ad = "0" // 非广告
     * ad = "1" // 广告
     * ```
     */
    var ad: String = "0"

    /**
     * [TODO]
     *
     * ```
     * l = listOf(1199, 1199, 0, 0, 0)
     * ```
     */
    var l: List<Int> = listOf(1199, 1199, 0, 0, 0)

    @Serializable
    data class Data(
        @SerialName("ad") val ad: String,
        @SerialName("l") val l: List<Int>,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("t") val t: String = "0",
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            ad = ad,
            l = l,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

