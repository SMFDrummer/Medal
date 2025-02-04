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
 * 超 Z 联赛商店进入与刷新
 *
 * @property ad 广告刷新
 * @property c [TODO]
 * @property s [TODO]
 */
@PacketIdentifier("V382")
class V382(override val user: User) : Packet() {
    override val identifier: String = "V382"

    /**
     * 广告刷新
     *
     * ```
     * ad = "0" // 非广告刷新
     * ad = "1" // 广告刷新
     * ```
     */
    var ad: String = "0"

    /**
     * [TODO]
     *
     * ```
     * c = "0" // 打开商店
     * c = "10" // 刷新奖励
     * ```
     */
    var c: String = "0"

    /**
     * [TODO]
     *
     * ```
     * s = "0" // 打开商店
     * s = "1" // 刷新奖励
     * ```
     */
    var s: String = "0"

    @Serializable
    data class Data(
        @SerialName("ad") val ad: String,
        @SerialName("c") val c: String,
        @SerialName("pi") val pi: String,
        @SerialName("s") val s: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            ad = ad,
            c = c,
            s = s,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

