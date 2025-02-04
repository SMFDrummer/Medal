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
 * 追击商店兑换刷新
 *
 * @property ad 领取状态
 * @property c [TODO]
 * @property s [TODO]
 */
@PacketIdentifier("V928")
class V928(override val user: User) : Packet() {
    override val identifier: String = "V928"

    /**
     * 领取状态
     *
     * ```
     * ad = "0" // 花费刷新或领取
     * ad = "1" // 广告刷新
     * ```
     */
    var ad: String = "1"

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
     * s = "1"
     * ```
     */
    var s: String = "1"

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
            pi = user.credential.personalId,
            s = s,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

