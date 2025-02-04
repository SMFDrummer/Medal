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
 * （月）每日签到或补签
 *
 * @property gt 签到或补签状态
 */
@PacketIdentifier("V389")
class V389(override val user: User) : Packet() {
    override val identifier: String = "V389"

    /**
     * 签到或补签状态
     *
     * ```
     * gt = "1" // 每日签到
     * gt = "4" // 补签
     * ```
     */
    var gt: String = "1"

    @Serializable
    data class Data(
        @SerialName("gt") val gt: String,
        @SerialName("hg") val hg: String = "1",
        @SerialName("lc") val lc: String = "1",
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
    )

    override fun build(): Pair<String, JsonObject> = identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
        Data(
            gt = gt,
            pi = user.credential.personalId,
            sk = user.credential.securityKey,
            ui = user.credential.userId,
        )
    ).jsonObject
}

