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
 * 植物探险状态
 *
 * @property f 探险状态
 * @property wi 探险世界 id
 */
@PacketIdentifier("V282")
class V282(override val user: User) : Packet() {
    override val identifier: String = "V282"

    /**
     * 探险状态
     *
     * ```
     * f = "1" // 消耗 20 钻石完成
     * f = "6" // 钻石购买探险次数
     * f = "7 // 广告增加探险次数
     * ```
     */
    var f: String = ""

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
        @SerialName("f") val f: String,
        @SerialName("pi") val pi: String,
        @SerialName("sk") val sk: String,
        @SerialName("ui") val ui: String,
        @SerialName("wi") val wi: String,
    )

    override fun build(): Pair<String, JsonObject> =
        identifier to Json.by(JsonFeature.EncodeDefaults).encodeToJsonElement(
            Data(
                f = f,
                wi = wi,
                pi = user.credential.personalId,
                sk = user.credential.securityKey,
                ui = user.credential.userId,
            )
        ).jsonObject

}