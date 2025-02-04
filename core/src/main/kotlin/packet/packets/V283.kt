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
 * 植物探险领取
 *
 * @property ad 领取状态
 * @property wi 探险世界 id
 */
@PacketIdentifier("V283")
class V283(override val user: User) : Packet() {
    override val identifier: String = "V283"

    /**
     * 领取状态
     *
     * ```
     * ad = "0" // 普通领取
     * ad = "1" // 翻倍领取
     * ```
     */
    var ad: String = "0"

    /**
     * 探险世界 id
     *
     * ```
     * wi = "10427"
     * ```
     */
    var wi: String = "10427"

    @Serializable
    data class Data(
        @SerialName("ad") val ad: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
        @SerialName("wi") val wi: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            ad = ad,
            wi = wi,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

